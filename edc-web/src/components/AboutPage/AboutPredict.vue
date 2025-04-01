<script setup>
import router from '../../router/index.js'

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
    <img style="display: flex;justify-content: center;width: 80%;margin-left: 10%" src="../../assets/predictor1.png">
<!--    <el-image style="width: 1100px;" :src=predictor1  />-->
    <img style="display: flex;justify-content: center;width:80%;margin-left: 10%" src="../../assets/predictor2.png">
<!--    <el-image style="width: 1100px;" :src=predictor2  />-->

  <div style="display: flex;
                    justify-content: center;
                    padding: 0 20px; /* 左右间距为 20px，根据需要调整 */">
    <p class="predict-text"> Screening for EDCs needs to be supported by a large amount of experimental data. The EDC database covers 13,307 qualitative data from standard screening tests and 6,713 quantitative data from standard screening tests that meet the data requirements. We also combined five machine learning algorithms to construct an optimal model of test endpoint events to predict and fill data gaps, and a total of 64,336 qualitative data were filled. The compilation and information sharing of test data was promoted, providing data support for the promotion of rapid screening of EDCs.
      <br>
    </p>
  </div>



</template>

<style scoped>
.predict-text {
  /* 中间介绍字体的样式*/
  font-size: 18px;
  margin-top: 20px;
  width: 80%; /* 设置最大宽度，以避免文本过长 */
  text-align: justify; /* 文本两端对齐 */
  line-height: 200%; /* 设置行高 */

}
</style>
