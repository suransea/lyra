import json
import re

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
    result = ['┌', '─' * (MARGIN_WIDTH * 2 + widths[0])]
    for i in range(1, col_count):
        result.append('┬')
        result.append('─' * (MARGIN_WIDTH * 2 + widths[i]))
    result.append('┐\n')

    # head data
    for i in range(0, col_count):
        item = ''
        if i < len(items[0]):
            item = str(items[0][i])
        result.append('│')
        result.append(' ' * MARGIN_WIDTH)
        result.append(item)
        result.append(' ' * (widths[i] - _width(item) + MARGIN_WIDTH))
    result.append('│\n')

    # span
    result.append('├')
    result.append('─' * (MARGIN_WIDTH * 2 + widths[0]))
    for i in range(1, col_count):
        result.append('┼')
        result.append('─' * (MARGIN_WIDTH * 2 + widths[i]))
    result.append('┤\n')

    # data lines
    items.remove(items[0])
    for row in items:
        for i in range(0, col_count):
            item = ''
            if i < len(row):
                item = str(row[i])
            result.append('│')
            result.append(' ' * MARGIN_WIDTH)
            result.append(item)
            result.append(' ' * (widths[i] - _width(item) + MARGIN_WIDTH))
        result.append('│\n')

    # tail
    result.append('└')
    result.append('─' * (MARGIN_WIDTH * 2 + widths[0]))
    for i in range(1, col_count):
        result.append('┴')
        result.append('─' * (MARGIN_WIDTH * 2 + widths[i]))
    result.append('┘\n')
    result.append(str(len(items)))
    result.append(' rows in set.\n\n')
    result.append('Consumption of time : ')
    result.append(str(rsp['time'] / 1000))
    result.append('s')
    print(''.join(result))


def _width(content):
    count = len(re.findall(r'[\u4e00-\u9fa5]', content))
    return len(content.encode('utf-8')) - count
