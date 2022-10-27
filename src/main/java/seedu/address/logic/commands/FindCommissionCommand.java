package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.commission.CompositePredicate;
import seedu.address.storage.Storage;
import seedu.address.ui.GuiTab;

public class FindCommissionCommand extends Command {
    public static final String COMMAND_WORD = "findcom";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all currently listed commissions that "
            + "has a title which contains any of the specified keywords (case-insensitive) and "
            + "includes all of the specified tags under -all and "
            + "at least one of the tags under -any.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]... -all TAG [MORE_TAGS]... -any TAG [MORE_TAGS]\n"
            + "Example: " + COMMAND_WORD + " k/Berserk k/Fate -all t/tag1 t/tag2 t/tag3 -any t/tag4 t/tag5";

    private final CompositePredicate predicate;

    public FindCommissionCommand(CompositePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, Storage...storage) {
        requireNonNull(model);
        model.updateFilteredCommissionList(predicate);
        model.selectTab(GuiTab.COMMISSION);
        return new CommandResult(String.format(Messages.MESSAGE_COMMISSIONS_LISTED_OVERVIEW,
                model.getFilteredCommissionList().size()));
    }
}
