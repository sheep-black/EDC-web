<template>
  <div class="common-layout">
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="contact-main">
        <p style="font-size: 35px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;">
          Contact us
        </p>
        <div style="display: flex;
                    justify-content: center;
                    padding: 0 20px; /* 左右间距为 20px，根据需要调整 */">
          <p class="contact-text">
            All chemical information can be found on this platform. If needed, please contact us for further communication and data acquisition.
            <br>
            <b>Authors</b>: Haoyue Tan, Tong Bao, Jinsha Jin, Rong Zhang, and Wei Shi.
            <br>
            <b>Corresponding authors</b>: Haoyue Tan (njutanhaoyue@nju.edu.cn) and Wei Shi (njushiwei@nju.edu.cn)
            <br>
            <b>Address</b>:
            <br>1. State Key Laboratory of Pollution Control and Resources Reuse, School of Environment, Nanjing University, Nanjing 210023, Jiangsu, China.
            <br>2. Jiangsu Province Ecology and Environment Protection Key Laboratory of Chemical Safety and Health Risk, Nanjing 210023, Jiangsu, China.
          </p>
        </div>


      </el-main>

    </div>
    <FooterNav />

  </div>

</template>

<script lang="ts" setup>
import router from '../router'
import { ref } from 'vue'
import HeaderNav from "@/components/HeaderNav.vue";
import FooterNav from "@/components/FooterNav.vue";

const activeIndex = ref('4')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}


</script>

<style>
.flex-grow {
  flex-grow: 1;
}
.contact-text {
  /* 中间介绍字体的样式*/
  font-size: 18px;
  max-width: 880px; /* 设置最大宽度，以避免文本过长 */
  text-align: justify; /* 文本两端对齐 */
  line-height: 200%; /* 设置行高 */
}
.contact-main{
  /* 设置图片作为背景 */
  background-image: url('../assets/contact_back.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height: 90vh;
  background-size: cover;
  background-position: center;
  /* //height: 500px; 根据需要设置高度 */
}

.Contact_footer {
  min-width: 1080px;
  margin-left: -8px;
  margin-right: -8px;
  margin-bottom: -8px;
  background-color: #2b5e8d;
  padding: 15px 0;
  text-align: center;
}

.footer-content {
  display: flex;
  justify-content: space-around;
  max-width: 1200px;
  margin: 0 auto;
}

.footer-section {
  flex: 1;
}

.footer-section h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.footer-section p {
  margin: 5px 0;
}

.footer-section ul {
  list-style-type: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 5px;
}

.footer-section ul li a {
  color: #ffffff;
  text-decoration: none;
}

.footer-section ul li a:hover {
  text-decoration: underline;
}


</style>
