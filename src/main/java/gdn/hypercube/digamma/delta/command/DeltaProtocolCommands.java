package gdn.hypercube.digamma.delta.command;

import gdn.hypercube.digamma.component.PlayerEventFlagsComponent;
import gdn.hypercube.digamma.init.DigammaProjectLoader;
import gdn.hypercube.digamma.delta.DeltaProtocolBootSequence;
import gdn.hypercube.epsilon.core.util.Argument;
import gdn.hypercube.epsilon.core.util.EngineCommand;
import gdn.hypercube.epsilon.core.util.MemoryHelper;

public class DeltaProtocolCommands {
    EngineCommand EMDS = new EngineCommand(EngineCommand.Type.PLAFORM_SPECIFIC, 0x01, (_, _) -> DeltaProtocolBootSequence.DRAW_MENU = true); /* Enable Menu Draw State */
    EngineCommand DMDS = new EngineCommand(EngineCommand.Type.PLAFORM_SPECIFIC, 0x02, (_, _) -> DeltaProtocolBootSequence.DRAW_MENU = false); /* Disable Menu Draw State */

    EngineCommand VarFill = new EngineCommand(EngineCommand.Type.PLAFORM_SPECIFIC, 0x03, (engine, argv) -> {
        int index = (int) argv[0].value;
        int length = (int) argv[1].value;
        System.out.println("Length target: " + length);
        String target = DeltaProtocolBootSequence.VARIABLES.get(index).get();
        if (target.length() < length) length = target.length();
        target.getChars(0, length, engine.script, engine.ip+1);
    }, new Argument(Argument.Type.SHORT), new Argument(Argument.Type.BYTE));

    // Mom, can we have Store8? No, we have Store8 at home. Store8 at home:
    @SuppressWarnings("DataFlowIssue")
    EngineCommand GetFlag = new EngineCommand(EngineCommand.Type.PLAFORM_SPECIFIC, 0x04, (engine, argv) -> {
        int base = MemoryHelper.registerBase((int) argv[1].value);
        int offset = (int) argv[2].value;
        PlayerEventFlagsComponent component = DigammaProjectLoader.EVENT_FLAGS.get(DeltaProtocolBootSequence.CLIENT.player);
        boolean active = component.get((char) argv[0].value);
        MemoryHelper.writeByteAt(engine.memory, base, offset, active ? 1 : 0);
    }, new Argument(Argument.Type.SHORT), new Argument(Argument.Type.BYTE), new Argument(Argument.Type.BYTE));

    @SuppressWarnings("DataFlowIssue")
    EngineCommand SetFlag = new EngineCommand(EngineCommand.Type.PLAFORM_SPECIFIC, 0x05, (_, argv) -> {
        PlayerEventFlagsComponent component = DigammaProjectLoader.EVENT_FLAGS.get(DeltaProtocolBootSequence.CLIENT.player);
        component.set((char) argv[0].value, argv[1].value != 0);
    }, new Argument(Argument.Type.SHORT), new Argument(Argument.Type.BYTE));
}
