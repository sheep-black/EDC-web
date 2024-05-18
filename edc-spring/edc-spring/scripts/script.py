import sys

def process_string(input_string):
    # 处理字符串的逻辑
    return input_string[::-1]  # 例如，将字符串反转

if __name__ == "__main__":
    input_string = sys.argv[1]
    result = process_string(input_string)
    print(result)