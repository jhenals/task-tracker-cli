package utils;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ParserLexer {
    private static StreamTokenizer tokenizer;

    public static List<String> parseCommand(String inputCommand) throws IOException {
        List<String> tokens= new ArrayList<>();

        StringReader sr = new StringReader(inputCommand);
        tokenizer = new StreamTokenizer(sr);
        configure( tokenizer );

        int currentToken;
        String token;

        while ((currentToken = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
            switch (currentToken) {
                case StreamTokenizer.TT_WORD:
                    token = tokenizer.sval;
                    tokens.add(token);
                    break;

                case StreamTokenizer.TT_NUMBER:
                    token = String.valueOf((int) tokenizer.nval);
                    tokens.add(token);
                    break;

                case '\"':
                    StringBuilder sb = new StringBuilder();
                    sb.append(tokenizer.sval);
                    while ((currentToken = tokenizer.nextToken()) != StreamTokenizer.TT_EOF && currentToken != '\"') {
                        sb.append(tokenizer.sval);
                    }
                    if (currentToken == '\"') {
                        sb.append(tokenizer.sval);
                    }
                    tokens.add(sb.toString());
                    break;

                default:
                    break;
            }
        }
        return tokens;
    }

    private static void configure(StreamTokenizer tokenizer) {
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');
        tokenizer.wordChars('0', '9');
        tokenizer.whitespaceChars(0, ' ');
    }
}
