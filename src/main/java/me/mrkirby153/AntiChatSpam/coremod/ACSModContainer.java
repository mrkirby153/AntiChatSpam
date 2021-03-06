package me.mrkirby153.AntiChatSpam.coremod;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import me.mrkirby153.AntiChatSpam.reference.Strings;

public class ACSModContainer extends DummyModContainer{


    public ACSModContainer(){
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "ACSCoremod";
        meta.name = "AntiChatSpam Coremod";
        meta.version = Strings.VERSION;
        meta.authorList.add("mrkirby153");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
