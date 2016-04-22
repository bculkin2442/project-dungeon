package bac.crawler.api.impl.parsers;

import java.io.InputStream;

import bjc.utils.data.IPair;
import bjc.utils.funcdata.FunctionalStringTokenizer;
import bjc.utils.funcutils.ListUtils;
import bjc.utils.parserutils.RuleBasedConfigReader;

import bac.crawler.api.IDescriber;
import bac.crawler.api.impl.GenericDescriber;

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
	 * @param tokenizer
	 *            The string tokenizer with the rest of the description
	 * @param currentState
	 *            The current parser states
	 */
	private static void continueDescription(
			FunctionalStringTokenizer tokenizer,
			DescriberState currentState) {
		String descriptionPart = ListUtils
				.collapseTokens(tokenizer.toList(), " ");

		currentState.continueDesc(descriptionPart);
	}

	/**
	 * End parsing a description
	 * 
	 * @param currentState
	 *            The current state of the parser
	 */
	private static void endDesc(DescriberState currentState) {
		currentState.endDesc();
	}

	/**
	 * Parse a describer from a provided stream
	 * 
	 * @param inputSource
	 *            The stream to parse from
	 * @return A describer generated from the provided stream
	 */
	public static GenericDescriber parseFile(InputStream inputSource) {
		DescriberState readState = reader.fromStream(inputSource,
				new DescriberState());

		return new GenericDescriber(readState.getResults());
	}

	/**
	 * Start parsing a description
	 * 
	 * @param tokenizer
	 *            The string tokenizer with the rest of the description
	 *            tokens
	 * @param initPair
	 *            A pair of the initial token and reader state
	 */
	private static void startDescription(
			FunctionalStringTokenizer tokenizer,
			IPair<String, DescriberState> initPair) {
		String probabilityString = initPair.getLeft();

		int prob = Integer.parseInt(probabilityString);

		String descriptionPart = ListUtils
				.collapseTokens(tokenizer.toList(), " ");

		initPair.getRight().startDesc(prob, descriptionPart);
	}
}