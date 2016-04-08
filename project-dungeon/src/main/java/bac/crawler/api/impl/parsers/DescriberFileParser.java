package bac.crawler.api.impl.parsers;

import java.io.InputStream;

import bac.crawler.api.IDescriber;
import bac.crawler.api.impl.GenericDescriber;
import bjc.utils.data.IPair;
import bjc.utils.funcdata.FunctionalStringTokenizer;
import bjc.utils.funcutils.ListUtils;
import bjc.utils.parserutils.RuleBasedConfigReader;

/**
 * Create implementations of {@link IDescriber} from text streams
 * 
 * @author ben
 *
 */
public class DescriberFileParser {
	/**
	 * The reader to use for driving parsing
	 */
	private static RuleBasedConfigReader<DescriberState> reader;

	static {
		/*
		 * Initialize the reader for usage
		 */

		reader = new RuleBasedConfigReader<>(
				DescriberFileParser::startDescription,
				DescriberFileParser::continueDescription,
				DescriberFileParser::endDesc);
	}

	/**
	 * Continue reading a description
	 * 
	 * @param fst
	 *            The string tokenizer with the rest of the description
	 * @param stat
	 *            The current parser states
	 */
	private static void continueDescription(FunctionalStringTokenizer fst,
			DescriberState stat) {
		String desc = ListUtils.collapseTokens(fst.toList(), " ");

		stat.continueDesc(desc);
	}

	/**
	 * End parsing a description
	 * 
	 * @param stat
	 *            The current state of the parser
	 */
	private static void endDesc(DescriberState stat) {
		stat.endDesc();
	}

	/**
	 * Parse a describer from a provided stream
	 * 
	 * @param is
	 *            The stream to parse from
	 * @return A describer generated from the provided stream
	 */
	public static GenericDescriber parseFile(InputStream is) {
		return new GenericDescriber(
				reader.fromStream(is, new DescriberState()).getResults());
	}

	/**
	 * Start parsing a description
	 * 
	 * @param fst
	 *            The string tokenizer with the rest of the description
	 *            tokens
	 * @param par
	 *            A pair of the initial token and reader state
	 */
	private static void startDescription(FunctionalStringTokenizer fst,
			IPair<String, DescriberState> par) {
		String probabilityString = par
				.merge((initialString, state) -> initialString);

		int prob = Integer.parseInt(probabilityString);

		String desc = ListUtils.collapseTokens(fst.toList(), " ");

		par.doWith((initialString, state) -> {
			state.startDesc(prob, desc);
		});
	}
}