<script setup>
import axios from "axios";

const databaseimage ="/src/assets/database-image.png"
const DownloadAOPData = async (filename) => {
  // 处理搜索逻辑，这里只是简单地打印搜索关键词
  console.info('文件名:',filename);
  const url =`/download?fileName=${filename}`;
  try {
    const response = await axios.get(url, { responseType: 'blob' });
    const downloadUrl = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = downloadUrl;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    link.remove();
  } catch (error) {
    console.error('Error downloading file:', error);
  }
};
</script>

<template>
  <p style="font-size: 20px;
                  padding: 0 20px;
                  font-weight: bold;
                  display: flex;
                  color: #000000;">
    EDC Database
  </p>
  <el-divider border-style="dashed" />
  <div style="display: flex;justify-content: center;">
    <el-image style="width: 1100px;" :src=databaseimage  />
  </div>
  <div style="display: flex;
                    justify-content: center;
                    padding: 0 20px; /* 左右间距为 20px，根据需要调整 */">
    <p class="database-text"> Screening for EDCs needs to be supported by a large amount of experimental data. The EDC database covers 13,307 qualitative data from standard screening tests and 6,713 quantitative data from standard screening tests that meet the data requirements. We also combined five machine learning algorithms to construct an optimal model of test endpoint events to predict and fill data gaps, and a total of 64,336 qualitative data were filled. The compilation and information sharing of test data was promoted, providing data support for the promotion of rapid screening of EDCs.
      <br>
      <div style="display: flex;justify-content: center;">
        <el-row :gutter="50">
          <el-col :span="12">
            <el-link type="primary" @click="DownloadAOPData('test_data.xlsx')">
              <el-icon size="20" style="margin-right: 5px"><Memo /></el-icon>
              <p style="font-size: 18px;
                        text-align: justify; /* 文本两端对齐 */
                        line-height: 1%; /* 设置行高 */
                        ">
                EDC AOP Network Data Download
              </p>
              <el-icon size="20" style="margin-left: 5px"><Memo /></el-icon>
            </el-link>
          </el-col>
          <el-col :span="12">
            <el-link type="primary"  @click="DownloadAOPData('test_data.xlsx')">
              <el-icon size="20" style="margin-right: 5px"><Memo /></el-icon>
              <p style="font-size: 18px;
                        text-align: justify; /* 文本两端对齐 */
                        line-height: 1%; /* 设置行高 */
                        ">
                EDC AOP Network Data Download
              </p>
              <el-icon size="20" style="margin-left: 5px"><Memo /></el-icon>
            </el-link>
          </el-col>
        </el-row>
      </div>


    </p>
  </div>



</template>

<style scoped>
.database-text {
  /* 中间介绍字体的样式*/
  font-size: 18px;
  margin-top: 40px;
  max-width: 1000px; /* 设置最大宽度，以避免文本过长 */
  text-align: justify; /* 文本两端对齐 */
  line-height: 200%; /* 设置行高 */

}
</style>
