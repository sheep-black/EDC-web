<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <!--        头部菜单栏-->
        <el-menu
            :default-active="activeIndex"
            style="min-width: 1080px;margin-left: -25px;margin-right: -25px;margin-top: -5px"
            mode="horizontal"
            background-color="#1A6B9AFF"
            text-color="#fff"
            active-text-color="#ffcc66"
            :ellipsis="false"
            @select="handleSelect"
        >
          <el-menu-item index="0" @click="router.push('/')">
            <!--      菜单左侧标志-->
            <el-icon
                color="#409efc"
                :size="30">
              <SwitchFilled />
            </el-icon>
            <p style="font-size: 18px; font-style: italic; margin-left: 6px;font-weight: bold;text-shadow: 1px 1px 2px black;">
              EDC  Database & Network & Predictor
            </p>
          </el-menu-item>
          <div class="flex-grow" />
          <el-sub-menu index="1">
            <template #title >
              <el-icon :size="20"><Search /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Search
              </p>
            </template>
            <el-menu-item index="1-1" style="justify-content: center;"  @click="router.push('/SearchAOP')">
              <p style="justify-content: center;">EDC-AOP</p>
            </el-menu-item>
            <el-menu-item index="1-2" style="justify-content: center;" @click="router.push('/SearchDATA')">
              <p style="justify-content: center;">EDC-DATA</p>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="2" @click="router.push('/Predict')">
            <template #title>
              <el-icon :size="20"><Odometer /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Predict
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="3" @click="router.push('/About')">
            <template #title>
              <el-icon :size="20"><InfoFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                About
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="4" @click="router.push('/Contact')">
            <template #title>
              <el-icon :size="20"><UserFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Contact
              </p>
            </template>
          </el-menu-item>
        </el-menu>
      </el-header>
      <el-main class="contact-main">
        <p style="font-size: 25px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1B497BFF;">
          Contact us
        </p>
        <div style="display: flex;
                    justify-content: center;
                    padding: 0 20px; /* 左右间距为 20px，根据需要调整 */">
          <p class="contact-text"> All chemical information can be found on this platform. If needed, please contact us for further communication and data acquisition. Contact: Prof. Shi Wei, School of Environment, Nanjing University (njushiwei@nju.edu.cn)；

          </p>
        </div>


      </el-main>

    </el-container>
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;">Copyright</h3>
          <el-divider />
          <p style="color: #ffffff;">All Rights © 2024</p>
          <p style="color: #ffffff;">Reserved</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;">Contact</h3>
          <el-divider />
          <p style="color: #ffffff;">E-mail：example@example.com</p>
          <p style="color: #ffffff;">Tel：123-456-7890</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;">Registration</h3>
          <el-divider />
          <ul>
            <li><a href="#">2024--</a></li>
            <li><a href="#">苏ICP备000000号</a></li>
          </ul>
        </div>
      </div>
    </footer>

  </div>

</template>

<script lang="ts" setup>
import router from '../router'
import { ref } from 'vue'

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
  max-width: 800px; /* 设置最大宽度，以避免文本过长 */
  text-align: justify; /* 文本两端对齐 */
  line-height: 200%; /* 设置行高 */
}
.contact-main{
  background-image: url('../assets/background4.png');
  min-width: 1080px;
  min-height: 75vh;
  background-size: cover;
  background-position: center;
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
