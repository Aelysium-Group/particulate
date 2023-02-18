import { ASCIIAlphabet } from "./ASCIIAlphabet.js";
import packageJSON from "../../package.json";
import chalk from 'chalk';

export class Lang {
    static readonly BORDER = "█████████████████████████████████████████████████████████████████████████████████████████████████";
    static readonly SPACING = "";

    static readonly NODE_WORDMARK = [
        chalk.bold(chalk.green("                                                                           _")),
        chalk.bold(chalk.green("                                                                          | |")),
        chalk.bold(chalk.green("                                                           _ __   ___   __| | ___")),
        chalk.bold(chalk.green("                                                          | '_ \\ / _ \\ / _` |/ _ \\")),
        chalk.bold(chalk.green("                                                          | | | | (_) | (_| |  __/")),
        chalk.bold(chalk.green("                                                          |_| |_|\\___/ \\__,_|\\___| ")) + chalk.yellow("Version " + packageJSON.version),
    ];

    static readonly MAIN_WORDMARK = [
        chalk.gray(this.BORDER),
        this.SPACING,
        ASCIIAlphabet.generate("PARTICULATE",(string) => chalk.blueBright(string)),
        this.NODE_WORDMARK,
        this.SPACING,
        chalk.gray(this.BORDER),
        this.SPACING,
        chalk.yellow("Developed by Aelysium | Nathan M."),
        this.SPACING,
        chalk.gray(this.BORDER),
        this.SPACING
    ];

    /**
     * Takes a message, loops through it and sends the current string row to a callback for handling.
     * @param message Message to process.
     * @param callback A callback with "row" as a parameter.
     */
    static print = (message: any[], callback: (currentRow: string) => void) => {
        for (const row of message)
            if(Array.isArray(row))
                this.print(row, (currentRow) => callback(currentRow))
            else
                callback(row);
    }
}