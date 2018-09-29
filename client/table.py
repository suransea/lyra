import json
import re
import sys

MARGIN_WIDTH = 2


def print_tb(rsp):
    lists = rsp['outcome']
    try:
        items = json.loads(lists)
    except json.JSONDecodeError:
        print(lists)
        return
    widths = []
    for head in items[0]:
        widths.append(_width(head))
    col_count = len(widths)
    for row in items:
        for i in range(0, len(row)):
            if _width(row[i]) > widths[i]:
                widths[i] = _width(row[i])

    # head
    sys.stdout.write('┌')
    sys.stdout.write('─' * (MARGIN_WIDTH * 2 + widths[0]))
    for i in range(1, col_count):
        sys.stdout.write('┬')
        sys.stdout.write('─' * (MARGIN_WIDTH * 2 + widths[i]))
    sys.stdout.write('┐\n')

    # head data
    for i in range(0, col_count):
        item = ''
        if i < len(items[0]):
            item = str(items[0][i])
        sys.stdout.write('│')
        sys.stdout.write(' ' * MARGIN_WIDTH)
        sys.stdout.write(item)
        sys.stdout.write(' ' * (widths[i] - _width(item) + MARGIN_WIDTH))
    sys.stdout.write('│\n')

    # span
    sys.stdout.write('├')
    sys.stdout.write('─' * (MARGIN_WIDTH * 2 + widths[0]))
    for i in range(1, col_count):
        sys.stdout.write('┼')
        sys.stdout.write('─' * (MARGIN_WIDTH * 2 + widths[i]))
    sys.stdout.write('┤\n')

    # data lines
    items.remove(items[0])
    for row in items:
        for i in range(0, col_count):
            item = ''
            if i < len(row):
                item = str(row[i])
            sys.stdout.write('│')
            sys.stdout.write(' ' * MARGIN_WIDTH)
            sys.stdout.write(item)
            sys.stdout.write(' ' * (widths[i] - _width(item) + MARGIN_WIDTH))
        sys.stdout.write('│\n')

    # tail
    sys.stdout.write('└')
    sys.stdout.write('─' * (MARGIN_WIDTH * 2 + widths[0]))
    for i in range(1, col_count):
        sys.stdout.write('┴')
        sys.stdout.write('─' * (MARGIN_WIDTH * 2 + widths[i]))
    sys.stdout.write('┘\n')
    sys.stdout.write(str(len(items)))
    sys.stdout.write(' rows in set.\n\n')
    sys.stdout.write('Consumption of time : ')
    sys.stdout.write(str(rsp['time'] / 1000))
    sys.stdout.write('s')
    sys.stdout.flush()
    print()


def _width(content):
    count = len(re.findall(r'[\u4e00-\u9fa5]', content))
    return len(content.encode('utf-8')) - count
