import pandas as pd

# 读取原始数据
# 读取原始数据，手动指定列名为 "AOPs" 和 "AOP_ID"
df = pd.read_excel("AOPs.xlsx", header=None, names=["AOPs", "AOP_ID"])

# 创建一个空的字典来存储每个事件所属的AOP序号
event_to_aop = {}

# 遍历每一行数据
for index, row in df.iterrows():
    aop_id = row["AOP_ID"]
    aop_str = row["AOPs"]
    events = aop_str.split(",")  # 按逗号分割AOP字符串，得到事件列表
    for event in events:
        if "event_" in event:
            event_id = int(event.split("_")[1])  # 将事件序号转换为整数类型
            event_to_aop.setdefault(event_id, set()).add(aop_id)

# 创建一个DataFrame来存储结果
result_df = pd.DataFrame(columns=["Event ID", "Associated AOPs"])
print(result_df)
# 将事件及其对应的AOP序号添加到结果DataFrame中
for event_id, aop_ids in event_to_aop.items():
    result_df = result_df.append({"Event ID": event_id, "Associated AOPs": ",".join(map(str, aop_ids))}, ignore_index=True)
result_df = result_df.sort_values(by="Event ID")
# 将结果写入到新的Excel文件
result_df.to_excel("output_file.xlsx", index=False)
