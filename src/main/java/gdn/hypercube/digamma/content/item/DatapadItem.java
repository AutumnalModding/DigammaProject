package gdn.hypercube.digamma.content.item;

import com.mojang.serialization.Codec;
import gdn.hypercube.digamma.delta.core.DeltaProtocolBootSequence;
import gdn.hypercube.epsilon.core.EpsilonEngine;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DatapadItem extends Item {
    public static boolean ACTIVATED = false;

    public static final ComponentType<Integer> ADDRESS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("digamma", "datapad/address"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public DatapadItem() {
        Item.Settings settings = new Item.Settings();
        settings.maxCount(1);
        settings.registryKey(RegistryKey.of(
                RegistryKeys.ITEM, Identifier.of("digamma", "datapad")
        ));

        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient()) {
            int address = stack.getOrDefault(ADDRESS, 0);
            if (!ACTIVATED) {
                DeltaProtocolBootSequence.DRAW_MAIN = true;
                DeltaProtocolBootSequence.LOCATION = DeltaProtocolBootSequence.DrawPosition.CENTER;
                DeltaProtocolBootSequence.ENGINE.reset();
                DeltaProtocolBootSequence.ENGINE.jump(address);
                DeltaProtocolBootSequence.LOGGER.info("Jumping to {}", address);
                DeltaProtocolBootSequence.ENGINE.status = EpsilonEngine.Status.RUNNING;
            } else {
                DeltaProtocolBootSequence.DRAW_MAIN = false;
                DeltaProtocolBootSequence.ENGINE.status = EpsilonEngine.Status.HALTED;
                stack.set(ADDRESS, DeltaProtocolBootSequence.ENGINE.ip);
            }
            ACTIVATED = !ACTIVATED;

            DeltaProtocolBootSequence.LOGGER.info("Set status to {}", DeltaProtocolBootSequence.DRAW_MAIN);

            return ActionResult.CONSUME;
        }

        return super.use(world, user, hand);
    }
}
