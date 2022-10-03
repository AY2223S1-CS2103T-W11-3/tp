package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CUSTOMERS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.commission.Commission;

/**
 * Deletes the commission of an existing customer in the ArtBuddy.
 */
public class DeleteCommissionCommand extends Command {

    public static final String COMMAND_WORD = "delcom";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the commission identified by the index number used in the displayed commission list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_COMMISSION_SUCCESS = "Deleted Commission: %1$s";

    private final Index targetIndex;

    public DeleteCommissionCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasActiveCustomer()) {
            throw new CommandException(Messages.MESSAGE_NO_ACTIVE_CUSTOMER);
        }

        List<Commission> lastShownList = model.getFilteredCommissionList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMISSION_DISPLAYED_INDEX);
        }

        Commission commissionToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteCommission(commissionToDelete);

        // This clones the person (shallow copy) so the GUI updates properly.
        model.updateActiveCustomer();
        model.updateFilteredCustomerList(PREDICATE_SHOW_ALL_CUSTOMERS);

        return new CommandResult(String.format(MESSAGE_DELETE_COMMISSION_SUCCESS, commissionToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommissionCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommissionCommand) other).targetIndex)); // state check
    }
}
