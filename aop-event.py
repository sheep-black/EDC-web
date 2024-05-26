import pandas as pd

# 读取原始数据
df = pd.read_excel("AOPs.xlsx", header=None, names=["AOPs", "AOP_ID"])

# 创建两个空的字典来分别存储每个类型事件所属的AOP序号
event_to_aop = {}
aop_wiki_event_to_aop = {}

# 遍历每一行数据
for index, row in df.iterrows():
    aop_id = row["AOP_ID"]
    aop_str = row["AOPs"]
    events = aop_str.split(",")  # 按逗号分割AOP字符串，得到事件列表
    for event in events:
        if "event_" in event:
            event_id = event.split("_")[1]  # 获取事件序号
            if "AOP-wiki-" in event:
                event_id = "AOP-wiki-event_" + event_id
                aop_wiki_event_to_aop.setdefault(event_id, set()).add(aop_id)
            else:
                event_to_aop.setdefault("event_" + event_id, set()).add(aop_id)

# 创建两个DataFrame来存储结果
result_df_event = pd.DataFrame(columns=["Event ID", "Associated AOPs"])
result_df_aop_wiki_event = pd.DataFrame(columns=["Event ID", "Associated AOPs"])

# 将事件及其对应的AOP序号添加到结果DataFrame中
for event_id, aop_ids in sorted(event_to_aop.items(), key=lambda x: int(x[0].split("_")[1])):
    result_df_event = result_df_event.append({"Event ID": event_id, "Associated AOPs": ",".join(map(str, aop_ids))}, ignore_index=True)

for event_id, aop_ids in sorted(aop_wiki_event_to_aop.items(), key=lambda x: int(x[0].split("_")[1])):
    result_df_aop_wiki_event = result_df_aop_wiki_event.append({"Event ID": event_id, "Associated AOPs": ",".join(map(str, aop_ids))}, ignore_index=True)

# 将结果写入到新的Excel文件
result_df_event.to_excel("output_file_event.xlsx", index=False)
result_df_aop_wiki_event.to_excel("output_file_aop_wiki_event.xlsx", index=False)
