package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.FindCommissionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.commission.CompositePredicate;
import seedu.address.model.tag.Tag;

public class FindCommissionCommandParser implements Parser<FindCommissionCommand> {
    private static final String INTERSECT_FLAG = "-all";
    private static final String UNION_FLAG = "-any";

    public FindCommissionCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        int intersectTagsGroupStart = trimmedArgs.indexOf(INTERSECT_FLAG);
        int intersectTagsContentStart = intersectTagsGroupStart + 4;
        int unionTagsGroupStart = trimmedArgs.indexOf(UNION_FLAG);
        int unionTagsContentStart = unionTagsGroupStart + 4;

        String rawKeywords = "";
        String rawIntersectTags = "";
        String rawUnionTags = "";
        if (intersectTagsGroupStart != -1) {
            rawKeywords = trimmedArgs.substring(0, intersectTagsGroupStart).trim();
            if (unionTagsGroupStart != -1) {
                rawIntersectTags = trimmedArgs.substring(intersectTagsGroupStart + 4, unionTagsGroupStart);
                rawUnionTags = unionTagsContentStart >= trimmedArgs.length() ? ""
                        : trimmedArgs.substring(unionTagsContentStart).trim();
            } else {
                rawIntersectTags = intersectTagsContentStart >= trimmedArgs.length() ? ""
                        : trimmedArgs.substring(intersectTagsContentStart).trim();
            }
        } else {
            if (unionTagsGroupStart != -1) {
                rawKeywords = trimmedArgs.substring(0, unionTagsGroupStart).trim();
                rawUnionTags = unionTagsContentStart >= trimmedArgs.length() ? ""
                        : trimmedArgs.substring(unionTagsContentStart).trim();
            } else {
                rawKeywords = trimmedArgs;
            }
        }

        List<String> keywords = new ArrayList<>();
        List<Tag> intersectTags = new ArrayList<>();
        List<Tag> unionTags = new ArrayList<>();
        if (!rawKeywords.isEmpty()) {
            rawKeywords = " " + rawKeywords;
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(rawKeywords, PREFIX_KEYWORD);
            List<String> givenKeywords = argMultimap.getAllValues(PREFIX_KEYWORD);
            keywords.addAll(givenKeywords);
        }

        if (!rawIntersectTags.isEmpty()) {
            rawIntersectTags = " " + rawIntersectTags;
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(rawIntersectTags, PREFIX_TAG);
            Set<Tag> givenIntersectTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            intersectTags.addAll(givenIntersectTags);
        }

        if (!rawUnionTags.isEmpty()) {
            rawUnionTags = " " + rawUnionTags;
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(rawUnionTags, PREFIX_TAG);
            Set<Tag> givenUnionTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            unionTags.addAll(givenUnionTags);
        }

        return new FindCommissionCommand(new CompositePredicate(keywords, intersectTags, unionTags));
    }
}
