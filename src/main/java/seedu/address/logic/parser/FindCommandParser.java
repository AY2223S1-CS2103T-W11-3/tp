package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.commission.CompositeCustomerPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {
    private static final String INTERSECT_FLAG = "-all";
    private static final String UNION_FLAG = "-any";

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        args = " " + args;
        List<FlagAndPosition> flagPositions = new ArrayList<>();
        int intersectTagsGroupStart = args.indexOf(INTERSECT_FLAG);
        if (intersectTagsGroupStart != -1) {
            flagPositions.add(new FlagAndPosition(INTERSECT_FLAG, PREFIX_TAG, intersectTagsGroupStart));
        }
        int unionTagsGroupStart = args.indexOf(UNION_FLAG);
        if (unionTagsGroupStart != -1) {
            flagPositions.add(new FlagAndPosition(UNION_FLAG, PREFIX_TAG, unionTagsGroupStart));
        }
        flagPositions.sort(FlagAndPosition::compareTo);

        Set<String> keywords = new HashSet<>();
        Set<Tag> intersectTags = new HashSet<>();
        Set<Tag> unionTags = new HashSet<>();

        ArgumentMultimap keywordMap;
        if (flagPositions.isEmpty()) {
            keywordMap = ArgumentTokenizer.tokenize(args, PREFIX_KEYWORD);
        } else {
            keywordMap = ArgumentTokenizer.tokenize(
                    args.substring(0, flagPositions.get(0).getPosition()), PREFIX_KEYWORD);
        }

        List<String> givenKeywords = keywordMap.getAllValues(PREFIX_KEYWORD);
        for (String givenKeyword : givenKeywords) {
            if (givenKeyword.isBlank()) {
                throw new ParseException(Messages.MESSAGE_KEYWORD_EMPTY);
            }
            keywords.add(givenKeyword);
        }

        for (int i = 0; i < flagPositions.size(); i++) {
            FlagAndPosition currFlag = flagPositions.get(i);
            // Last element
            ArgumentMultimap argMap;
            if (i == flagPositions.size() - 1) {
                argMap = ArgumentTokenizer.tokenize(
                        args.substring(currFlag.getPosition()), currFlag.getToken());
            } else {
                FlagAndPosition nextFlag = flagPositions.get(i + 1);
                argMap = ArgumentTokenizer.tokenize(
                        args.substring(currFlag.getPosition(), nextFlag.getPosition()), currFlag.getToken());
            }

            if (currFlag.getFlag().equals(INTERSECT_FLAG)) {
                Set<Tag> givenIntersectTags = ParserUtil.parseTags(argMap.getAllValues(currFlag.getToken()));
                intersectTags.addAll(givenIntersectTags);
            } else if (currFlag.getFlag().equals(UNION_FLAG)) {
                Set<Tag> givenUnionTags = ParserUtil.parseTags(argMap.getAllValues(currFlag.getToken()));
                unionTags.addAll(givenUnionTags);
            } else {
                throw new Error("Code should be unreachable");
            }
        }

        if (keywords.isEmpty() && intersectTags.isEmpty() && unionTags.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new CompositeCustomerPredicate(keywords, intersectTags, unionTags));
    }
}
