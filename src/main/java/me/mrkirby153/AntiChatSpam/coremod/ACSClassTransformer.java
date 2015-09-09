package me.mrkirby153.AntiChatSpam.coremod;

import me.mrkirby153.AntiChatSpam.regex.ChatHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class ACSClassTransformer implements IClassTransformer{

    private Logger logger;

    public ACSClassTransformer(){
        this.logger = LogManager.getLogger("ACS-Coremod");
    }

    private static final String[] classToTransform = {"net.minecraft.client.entity.EntityPlayerSP"};

    @Override
    public byte[] transform(String name, String deobfName, byte[] classBeingTransformed) {
        boolean isObfuscated = !name.equalsIgnoreCase(deobfName);
        int index = Arrays.asList(classToTransform).indexOf(deobfName);
        return index != -1 ? transform(index, classBeingTransformed, isObfuscated) : classBeingTransformed;
    }

    private byte[] transform(int index, byte[] classBeingTransformed, boolean obfuscated){
        logger.info("Transforming "+classToTransform[index]);
        try{
            ClassNode classNode = new ClassNode();
            ClassReader reader = new ClassReader(classBeingTransformed);
            reader.accept(classNode, 0);
            handleTransformation(index, classNode, obfuscated);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception e){
            logger.catching(Level.FATAL, e);
        }
        logger.warn("Something went wrong! Returning untransformed class");
        return classBeingTransformed;
    }

    private void handleTransformation(int index, ClassNode classNode, boolean obfuscated){
        switch (index){
            case 0:
                transformEntityPlayerSP(classNode, obfuscated);
        }
    }

    private void transformEntityPlayerSP(ClassNode entityPlayerSPClass, boolean obfuscated) {
        final String ADD_CHAT_COMPONENT_MESSAGE = (obfuscated) ? "b" : "addChatComponentMessage";
        final String ADD_CHAT_COMPONENT_DESCRIPTOR = (obfuscated) ? "(Lfj;)V" : "(Lnet/minecraft/util/IChatComponent;)V";
        // Find target node
        MethodNode targetMethod = findTargetNode(entityPlayerSPClass, ADD_CHAT_COMPONENT_MESSAGE, ADD_CHAT_COMPONENT_DESCRIPTOR);
        if(targetMethod == null)
            return;
        AbstractInsnNode targetNode = null;
        for(AbstractInsnNode instruction : targetMethod.instructions.toArray()){
            if(instruction.getOpcode() == ALOAD){
                if(((VarInsnNode) instruction).var == 0 && instruction.getNext().getOpcode() == GETFIELD){
                    targetNode = instruction;
                    break;
                }
            }
        }
        if(targetNode == null)
            return;
        /*
        Replace:
        this.mc.ingameGui.getChatGUI().printChatMessage(p_146105_1_);
        WITH:
        if(!ChatHandler.hasHandledChat(p_146105_1_){
            this.mc.ingameGui.getChatGUI().printChatMessage();
         }
         BYTECODE:
         REPLACE:
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", "mc", "Lnet/minecraft/client/Minecraft;");
            v.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "ingameGUI", "Lnet/minecraft/client/gui/GuiIngame;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/gui/GuiIngame", "getChatGUI", "()Lnet/minecraft/client/gui/GuiNewChat;", false);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/gui/GuiNewChat", "printChatMessage", "(Lnet/minecraft/util/IChatComponent;)V", false);
         WIT
            ALOAD 0
            INVOKESTATIC ChatHandler.hasHandledChat()
            IFNE newLabelNode
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", "mc", "Lnet/minecraft/client/Minecraft;");
            v.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "ingameGUI", "Lnet/minecraft/client/gui/GuiIngame;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/gui/GuiIngame", "getChatGUI", "()Lnet/minecraft/client/gui/GuiNewChat;", false);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/gui/GuiNewChat", "printChatMessage", "(Lnet/minecraft/util/IChatComponent;)V", false);
            LABEL newLabelNode
         */
        AbstractInsnNode invokeVirtualPrint = targetNode;
        for(int i = 0; i < 5; i++){
            invokeVirtualPrint = invokeVirtualPrint.getNext();
        }
        LabelNode newLabelNode = new LabelNode();
        InsnList toInsert = new InsnList();
        toInsert.add(new VarInsnNode(ALOAD, 1));
        final String HAS_HANDLED_CHAT_DESCRIPTOR = (obfuscated)? "(Lfj;)V" : "(Lnet/minecraft/util/IChatComponent;)V";
        toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ChatHandler.class), "hasHandledChat", HAS_HANDLED_CHAT_DESCRIPTOR, false));
        toInsert.add(new JumpInsnNode(IFNE, newLabelNode));

        targetMethod.instructions.insertBefore(targetNode, toInsert);
        targetMethod.instructions.insert(invokeVirtualPrint, newLabelNode);

        for(AbstractInsnNode n : targetMethod.instructions.toArray()){
            System.out.println(n.getOpcode());
        }
    }

    private MethodNode findTargetNode(ClassNode classNode, String methodName, String methodDescriptor) {
        logger.info(String.format("Finding method with name %s and descriptor %s", methodName, methodDescriptor));
       for(MethodNode method : classNode.methods){
           if(method.name.equals(methodName) && method.desc.equals(methodDescriptor)){
               logger.info("Found method with name "+method.name+" and descriptor "+method.desc);
               return method;
           }
       }
        return null;
    }
}
