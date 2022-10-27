package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.storage.Storage;
import seedu.address.ui.GuiTab;

/**
 * Lists all commissions in the address book to the user.
 */
public class ListCommissionCommand extends Command {

    public static final String COMMAND_WORD = "listcom";

    public static final String MESSAGE_SUCCESS = "Listed all commissions";


    @Override
    public CommandResult execute(Model model, Storage...storage) {
        requireNonNull(model);
        model.selectCustomer(null);
        model.selectTab(GuiTab.COMMISSION);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
