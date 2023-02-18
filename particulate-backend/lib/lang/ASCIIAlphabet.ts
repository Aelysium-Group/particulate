export class ASCIIAlphabet {
    static readonly fontSize = 6;

    private static readonly EMPTY = [
        "",
        "",
        "",
        "",
        "",
        ""
    ];

    private static readonly WHITESPACE = [
        "     ",
        "     ",
        "     ",
        "     ",
        "     ",
        "     "
    ];

    private static readonly A = [
        " █████╗ ",
        "██╔══██╗",
        "███████║",
        "██╔══██║",
        "██║  ██║",
        "╚═╝  ╚═╝"
    ];
    private static readonly B = [
        "██████╗ ",
        "██╔══██╗",
        "██████╔╝",
        "██╔══██╗",
        "██████╔╝",
        "╚═════╝ "
    ];
    private static readonly C = [
        " ██████╗",
        "██╔════╝",
        "██║     ",
        "██║     ",
        "╚██████╗",
        " ╚═════╝"
    ];
    private static readonly D = [
        "██████╗ ",
        "██╔══██╗",
        "██║  ██║",
        "██║  ██║",
        "██████╔╝",
        "╚═════╝ "
    ];
    private static readonly E = [
        "███████╗",
        "██╔════╝",
        "█████╗  ",
        "██╔══╝  ",
        "███████╗",
        "╚══════╝"
    ];
    private static readonly F = [
        "███████╗",
        "██╔════╝",
        "█████╗  ",
        "██╔══╝  ",
        "██║     ",
        "╚═╝     "
    ];
    private static readonly G = [
        " ██████╗ ",
        "██╔════╝ ",
        "██║  ███╗",
        "██║   ██║",
        "╚██████╔╝",
        " ╚═════╝ "
    ];
    private static readonly H = [
        "██╗  ██╗",
        "██║  ██║",
        "███████║",
        "██╔══██║",
        "██║  ██║",
        "╚═╝  ╚═╝"
    ];
    private static readonly I = [
        "██╗",
        "██║",
        "██║",
        "██║",
        "██║",
        "╚═╝"
    ];
    private static readonly J = [
        "     ██╗",
        "     ██║",
        "     ██║",
        "██   ██║",
        "╚█████╔╝",
        " ╚════╝ "
    ];
    private static readonly K = [
        "██╗  ██╗",
        "██║ ██╔╝",
        "█████╔╝ ",
        "██╔═██╗ ",
        "██║  ██╗",
        "╚═╝  ╚═╝"
    ];
    private static readonly L = [
        "██╗     ",
        "██║     ",
        "██║     ",
        "██║     ",
        "███████╗",
        "╚══════╝"
    ];
    private static readonly M = [
        "███╗   ███╗",
        "████╗ ████║",
        "██╔████╔██║",
        "██║╚██╔╝██║",
        "██║ ╚═╝ ██║",
        "╚═╝     ╚═╝"
    ];
    private static readonly N = [
        "███╗   ██╗",
        "████╗  ██║",
        "██╔██╗ ██║",
        "██║╚██╗██║",
        "██║ ╚████║",
        "╚═╝  ╚═══╝"
    ];
    private static readonly O = [
        " ██████╗ ",
        "██╔═══██╗",
        "██║   ██║",
        "██║   ██║",
        "╚██████╔╝",
        " ╚═════╝ "
    ];
    private static readonly P = [
        "██████╗ ",
        "██╔══██╗",
        "██████╔╝",
        "██╔═══╝ ",
        "██║     ",
        "╚═╝     "
    ];
    private static readonly Q = [
        " ██████╗ ",
        "██╔═══██╗",
        "██║   ██║",
        "██║▄▄ ██║",
        "╚██████╔╝",
        " ╚══▀▀═╝ "
    ];
    private static readonly R = [
        "██████╗ ",
        "██╔══██╗",
        "██████╔╝",
        "██╔══██╗",
        "██║  ██║",
        "╚═╝  ╚═╝"
    ];
    private static readonly S = [
        "███████╗",
        "██╔════╝",
        "███████╗",
        "╚════██║",
        "███████║",
        "╚══════╝"
    ];
    private static readonly T = [
        "████████╗",
        "╚══██╔══╝",
        "   ██║   ",
        "   ██║   ",
        "   ██║   ",
        "   ╚═╝   "
    ];
    private static readonly U = [
        "██╗   ██╗",
        "██║   ██║",
        "██║   ██║",
        "██║   ██║",
        "╚██████╔╝",
        " ╚═════╝ "
    ];
    private static readonly V = [
        "██╗   ██╗",
        "██║   ██║",
        "██║   ██║",
        "╚██╗ ██╔╝",
        " ╚████╔╝ ",
        "  ╚═══╝  "
    ];
    private static readonly W = [
        " ██╗    ██╗",
        " ██║    ██║",
        " ██║ █╗ ██║",
        " ██║███╗██║",
        " ╚███╔███╔╝",
        "  ╚══╝╚══╝ "
    ];
    private static readonly X = [
        "██╗  ██╗",
        "╚██╗██╔╝",
        " ╚███╔╝ ",
        " ██╔██╗ ",
        "██╔╝ ██╗",
        "╚═╝  ╚═╝"
    ];
    private static readonly Y = [
        "██╗   ██╗",
        "╚██╗ ██╔╝",
        " ╚████╔╝ ",
        "  ╚██╔╝  ",
        "   ██║   ",
        "   ╚═╝   "
    ];
    private static readonly Z = [
        "███████╗",
        "╚══███╔╝",
        "  ███╔╝ ",
        " ███╔╝  ",
        "███████╗",
        "╚══════╝"
    ];
    private static readonly PERIOD = [
        "     ",
        "     ",
        "     ",
        "████╗",
        "████║",
        "╚═══╝"
    ];
    private static readonly EXCLAMATION = [
        "██╗",
        "██║",
        "██║",
        "╚═╝",
        "██╗",
        "╚═╝"
    ];
    private static readonly QUESTION = [
        "██████╗ ",
        "╚════██╗",
        "  ▄███╔╝",
        "  ▀▀══╝ ",
        "  ██╗   ",
        "  ╚═╝   "
    ];
    private static readonly DASH = [
        "      ",
        "      ",
        "█████╗",
        "╚════╝",
        "      ",
        "      "
    ];
    private static readonly UNDERSCORE = [
        "        ",
        "        ",
        "        ",
        "        ",
        "███████╗",
        "╚══════╝"
    ];

    /**
     * Generates an ASCCII Character representation of the string.
     * @param string The string to generate from.
     * @param color The color that the output should be.
     * @returns An array of strings.
     */
    static generate = (string: string, callback?: (row: string) => string): string[] => {
        const chars = string.toUpperCase().split('');
        const generatedMessage: string[] = this.EMPTY;

        for(const character of chars) {
            const asciiCharacter = ASCIIAlphabet.alphabet[character];
            for (let index = 0; index < this.fontSize; index++) 
                generatedMessage[index] = generatedMessage[index] + " " + asciiCharacter[index];
        }

            
        if(callback == undefined) return generatedMessage;

        for (const row of generatedMessage)
            generatedMessage[generatedMessage.indexOf(row)] = callback(row);
        
        return generatedMessage;
    }

    /**
     * Takes an ascii string, loops through it and sends the current string row to a callback for handling.
     * @param asciiString The ascii string to process.
     * @param callback A callback with "row" as a parameter.
     */
    static print = (asciiString: string[], callback: (currentRow: string) => void) => {
        if(asciiString.length != ASCIIAlphabet.fontSize) throw new Error("You must provide a valid ascii string!");
        for (const row of asciiString) {
            callback(row);
        }
    }

    static readonly alphabet: {[key: string]: string[]} = {
        'A': ASCIIAlphabet.A,
        'B': ASCIIAlphabet.B,
        'C': ASCIIAlphabet.C,
        'D': ASCIIAlphabet.D,
        'E': ASCIIAlphabet.E,
        'F': ASCIIAlphabet.F,
        'G': ASCIIAlphabet.G,
        'H': ASCIIAlphabet.H,
        'I': ASCIIAlphabet.I,
        'J': ASCIIAlphabet.J,
        'K': ASCIIAlphabet.K,
        'L': ASCIIAlphabet.L,
        'M': ASCIIAlphabet.M,
        'N': ASCIIAlphabet.N,
        'O': ASCIIAlphabet.O,
        'P': ASCIIAlphabet.P,
        'Q': ASCIIAlphabet.Q,
        'R': ASCIIAlphabet.R,
        'S': ASCIIAlphabet.S,
        'T': ASCIIAlphabet.T,
        'U': ASCIIAlphabet.U,
        'V': ASCIIAlphabet.V,
        'W': ASCIIAlphabet.W,
        'X': ASCIIAlphabet.X,
        'Y': ASCIIAlphabet.Y,
        'Z': ASCIIAlphabet.Z,
        ' ': ASCIIAlphabet.WHITESPACE,
        '.': ASCIIAlphabet.PERIOD,
        '-': ASCIIAlphabet.DASH,
        '!': ASCIIAlphabet.EXCLAMATION,
        '?': ASCIIAlphabet.QUESTION,
        '_': ASCIIAlphabet.UNDERSCORE
    };
}
