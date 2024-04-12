<script setup>
const intro_image ="/src/assets/network-image.png"
</script>

<template>
  <p style="font-size: 20px;
                  padding: 0 20px;
                  font-weight: bold;
                  display: flex;
                  color: #000000;">
    EDC AOP Network
  </p>
  <el-divider border-style="dashed" />
  <el-row style="display: flex;justify-content: center;margin-top: -20px;" :gutter="10">
    <el-col :span="12">
      <div style="display: flex;
                    padding: 0 20px; /* 左右间距为 20px，根据需要调整 */">
        <p class="network-text">The mechanism of EDCs is complex. However, AOP can help deepen the understanding of the mechanisms of endocrine disruptor toxicity by cascading a range of biological effects caused by endocrine disruptors at different levels. Therefore, the mechanisms of action related to the four modes of action of EDCs (estrogen receptor interference, androgen receptor interference, thyroid hormone receptor interference, and steroid hormone synthesis interference) were collected based on manual searching and screening of databases as well as massive literature mining by natural language processing. Finally, a more complete AOP-network of current EDCs was constructed, containing 673 AOPs covering 1617 Key Event Relationships (KER), 59 Molecular Initiation Events (MIE), 624 Key Events (KE), and 181 Adverse Outcomes (AO). Event (KE), 624 Key Events (KE), and 181 Adverse Outcomes (AO).
          <br>
          <el-link type="primary" @click="this.$router.push('/Contact')">
            <el-icon size="20" style="margin-right: 5px"><Memo /></el-icon>
            <p style="font-size: 18px;
                        text-align: justify; /* 文本两端对齐 */
                        line-height: 1%; /* 设置行高 */
                        ">
              EDC AOP Network Data Download
            </p>
            <el-icon size="20" style="margin-left: 5px"><Memo /></el-icon>
          </el-link>
        </p>
      </div>
    </el-col>
    <el-col :span="12" style="display: flex;align-items: center;">
      <el-image style="display: flex;max-height: 100vh;width: auto" :src=intro_image  />
    </el-col>

  </el-row>


</template>

<style scoped>
.network-text {
  /* 中间介绍字体的样式*/
  font-size: 18px;
  max-width: 1000px; /* 设置最大宽度，以避免文本过长 */
  text-align: justify; /* 文本两端对齐 */
  line-height: 200%; /* 设置行高 */

}
</style>
