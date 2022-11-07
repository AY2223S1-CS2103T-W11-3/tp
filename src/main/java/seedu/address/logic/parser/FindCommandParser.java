package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_KEYWORD_EMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIND_ALL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIND_ANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommissionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.commission.CompositeCustomerPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap mainArgMultimap =
                ArgumentTokenizer.tokenize(" " + args, PREFIX_KEYWORD, PREFIX_FIND_ALL, PREFIX_FIND_ANY);

        List<String> rawKeywords = mainArgMultimap.getAllValues(PREFIX_KEYWORD);
        Optional<String> rawIntersectTags = mainArgMultimap.getValue(PREFIX_FIND_ALL);
        Optional<String> rawUnionTags = mainArgMultimap.getValue(PREFIX_FIND_ANY);

        if (rawKeywords.stream().anyMatch(keyword -> Objects.equals(keyword.strip(), ""))) {
            throw new ParseException(MESSAGE_KEYWORD_EMPTY);
        }

        if (rawKeywords.stream().anyMatch(keyword -> keyword.strip().contains(" "))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommissionCommand.MESSAGE_USAGE));
        }

        Set<String> keywords = new HashSet<>(rawKeywords);
        Set<Tag> intersectTags = new HashSet<>();
        Set<Tag> unionTags = new HashSet<>();

        if (rawIntersectTags.isPresent()) {
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(rawIntersectTags.map(rawCommand -> " " + rawCommand).orElse(""),
                            PREFIX_TAG);
            Set<Tag> givenIntersectTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            intersectTags.addAll(givenIntersectTags);
        }

        if (rawUnionTags.isPresent()) {
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(rawUnionTags.map(rawCommand -> " " + rawCommand).orElse(""), PREFIX_TAG);
            Set<Tag> givenUnionTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            unionTags.addAll(givenUnionTags);
        }

        if (keywords.isEmpty() && intersectTags.isEmpty() && unionTags.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new CompositeCustomerPredicate(keywords, intersectTags, unionTags));
    }

}
