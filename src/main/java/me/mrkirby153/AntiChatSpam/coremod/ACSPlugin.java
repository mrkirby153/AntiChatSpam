package me.mrkirby153.AntiChatSpam.coremod;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({"me.mrkirby153.AntiChatSpam.coremod"})
public class ACSPlugin implements IFMLLoadingPlugin{
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"me.mrkirby153.AntiChatSpam.coremod.ACSClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return "me.mrkirby153.AntiChatSpam.coremod.ACSModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
