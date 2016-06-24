# import click
import json
import re


CUTSCENE_DIR = '../src/project_rpg/database/cutscenes'


class Parser(object):
    """Parser that builds the JSON cutscene file.

    Attributes:
        cutscene (dict): The JSON cutscene.
        image (str): The current image in the cutscene.
    """

    def __init__(self):
        """Initializes the cutscene dictionary.
        """
        self.cutscene = {
            'scenes': []
        }
        self.image = 'null'

    def execute(self, command, argument):
        """Executes a command in the text file.

        Args:
            command (str): The name of the command.
            argument (str): The argument to pass into the command.
        """
        if command == 'image':
            self.image = argument
        else:
            raise SyntaxError('Unrecognized command: {command}.'.format(command))

    def parse(self, line):
        """Parses a line read from the raw text file.
    
        Args:
            line (str): The line of raw text (stripped of new line characters).
        """
        match = re.match(r'> (\w+) "(\w+)"', line)
        if match:
            command = match.group(1)
            argument = match.group(2)
            self.execute(command, argument)
        else:
            match = re.match(r'(\w+): "(.+)"', line)
            if not match:
                raise SyntaxError('Unable to read line: "{line}".'.format(line=line))
            self.cutscene['scenes'].append({
                'image': self.image,
                'line': match.group(2),
                'speaker': match.group(1)
            })

    def parse_and_write(self, path):
        """Reads in a raw text file, parses it, and writes the output to a JSON file. Assumes that there is a file called
        project_rpg/src/project_rpg/database/cutscenes/raw/<path>.txt. The output directory for the JSON file is
        project_rpg/src/project_rpg/database/cutscenes/<path>.json.
    
        Args:
            path (str): The name of the cutscene file.
        """
        raw_file_path = '{dir}/raw/{path}.txt'.format(dir=CUTSCENE_DIR, path=path)
        with open(raw_file_path, 'r') as txt_file:
            for line in txt_file:
                # Remove end of line characters.
                line = line.replace('\n', '')
                if line == '':
                    # Skip empty lines.
                    continue
                try:
                    self.parse(line)
                except SyntaxError:
                    import pdb; pdb.set_trace()
        self.write_to_output(path)

    def write_to_output(self, path):
        """Writes the cutscene to the JSON file, and also prints the output in stdout.

        Args:
            path (str): The name of the cutscene file.
        """
        output_file_path = '{dir}/{path}.json'.format(dir=CUTSCENE_DIR, path=path)
        json_str = json.dumps(self.cutscene, indent=4, sort_keys=True)
        print(json_str)
        with open(output_file_path, 'w') as json_file:
            json_file.write(json_str)


if __name__ == '__main__':
    parser = Parser()
    parser.parse_and_write('test')

