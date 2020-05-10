package net.rafkos.tools.eu4.euivlocaleparser.cli.commands

import net.rafkos.tools.eu4.euivlocaleparser.LocaleType
import net.rafkos.tools.eu4.euivlocaleparser.ProcessHelper
import net.rafkos.tools.eu4.euivlocaleparser.charsets.Charsets
import java.io.File

object FolderToYamlCommand : Command {
    override fun validForArguments(args: List<String>): Boolean {
        if (args.size != 3) {
            println("Incorrect number of arguments.")
            return false
        }
        val input = File(args[0])

        if (!input.isDirectory) {
            println("Input folder \"${input.canonicalPath}\" does not exist or is a file.")
            return false
        }

        val output = File(args[1])
        output.mkdirs()

        if (!output.isDirectory) {
            println("Output folder \"${output.canonicalPath}\" does not exist or is a file.")
            return false
        }

        if (input.canonicalPath == output.canonicalPath) {
            println("Input and output folders must be different.")
            return false
        }

        if (!Charsets.charsets.containsKey(args[2])) {
            println("Incorrect charset provided \"${args[2]}\".")
            return false
        }

        return true
    }

    override fun execute(args: List<String>) {
        val input = File(args[0])
        val output = File(args[1])
        val charset = Charsets.charsets[args[2]]
        val locales = ProcessHelper.loadDirectoryOfType(input, LocaleType.EUIV)
        val convertedLocales = ProcessHelper.convertLocalesToType(locales, charset!!, LocaleType.YAML)
        ProcessHelper.writeLocalesToDirectory(output, convertedLocales)
    }

    override val help: String = "<input folder> <output folder> <charset> - Converts .yml files inside input directory from EUIV format to standard YML format and writes them to output directory."
}