<template>
  <div  class="common-layout">
    <el-container >
      <el-header>
        <!--        头部菜单栏-->
        <el-menu
            :default-active="activeIndex"
            style="min-width: 1080px; margin-left: -25px;margin-right: -25px;margin-top: -5px"
            mode="horizontal"
            background-color="#1A6B9AFF"
            text-color="#fff"
            active-text-color="#ffcc66"
            :ellipsis="false"
            @select="handleSelect"
        >
          <el-menu-item index="0" @click="this.$router.push('/')">
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
            <el-menu-item index="1-1" style="justify-content: center;" @click="this.$router.push('/SearchAOP')">
              <p style="justify-content: center;" >EDC-AOP</p>
            </el-menu-item>
            <el-menu-item index="1-2" style="justify-content: center;" @click="this.$router.push('/SearchDATA')">
              <p style="justify-content: center;">EDC-DATA</p>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="2" @click="this.$router.push('/Predict')">
            <template #title>
              <el-icon :size="20"><Odometer /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Predict
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="3" @click="this.$router.push('/About')">
            <template #title>
              <el-icon :size="20"><InfoFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                About
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="4" @click="this.$router.push('/Contact')">
            <template #title>
              <el-icon :size="20"><UserFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Contact
              </p>
            </template>
          </el-menu-item>
        </el-menu>
      </el-header>
      <p style="font-size: 25px;
                  margin-left: 11%;
                  font-weight: bold;
                  justify-content: left;
                  display: flex;
                  color: #1B497BFF;">
        Research result
      </p>
      <el-divider style="margin-top: -5px;min-width: 1080px" />
      <el-main class="SearchResult-main">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card style="max-width: 480px;">
            <template #header>
              <div class="card-header">
                <span>Card name</span>
              </div>
            </template>
              <div style="display: flex;justify-content: center">
                <el-image style="width: 350px;" :src=intro_image  />
              </div>
            <template #footer>Footer content</template>
          </el-card>
          </el-col>
          <el-col :span="16">
            <p style="font-size: 25px;
                  margin-left: 50px;
                  font-weight: bold;
                  margin-top: -5px;
                  justify-content: left;
                  display: flex;
                  color: #1B497BFF;">
            Name
          </p>
              <div class="collapse">
                <el-collapse v-model="activeNames" class="custom-collapse">
                  <el-collapse-item title="Consistency" name="1">
                    <div>
                      Consistent with real life: in line with the process and logic of real
                      life, and comply with languages and habits that the users are used to;
                    </div>
                    <div>
                      Consistent within interface: all elements should be consistent, such
                      as: design style, icons and texts, position of elements, etc.
                    </div>
                  </el-collapse-item>
                  <el-collapse-item title="Feedback" name="2">
                    <div>
                      Operation feedback: enable the users to clearly perceive their
                      operations by style updates and interactive effects;
                    </div>
                    <div>
                      Visual feedback: reflect current state by updating or rearranging
                      elements of the page.
                    </div>
                  </el-collapse-item>
                  <el-collapse-item title="Efficiency" name="3">
                    <div>
                      Simplify the process: keep operating process simple and intuitive;
                    </div>
                    <div>
                      Definite and clear: enunciate your intentions clearly so that the
                      users can quickly understand and make decisions;
                    </div>
                    <div>
                      Easy to identify: the interface should be straightforward, which helps
                      the users to identify and frees them from memorizing and recalling.
                    </div>
                  </el-collapse-item>
                  <el-collapse-item title="Controllability" name="4">
                    <div>
                      Decision making: giving advices about operations is acceptable, but do
                      not make decisions for the users;
                    </div>
                    <div>
                      Controlled consequences: users should be granted the freedom to
                      operate, including canceling, aborting or terminating current
                      operation.
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
          </el-col>
        </el-row>

      </el-main>
    </el-container>
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 3px;">授权信息</h3>
          <el-divider />
          <p style="color: #ffffff;">版权所有 © 2024</p>
          <p style="color: #ffffff;">保留所有权利</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 3px;">联系方式</h3>
          <el-divider />
          <p style="color: #ffffff;">联系邮箱：example@example.com</p>
          <p style="color: #ffffff;">电话：123-456-7890</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 3px;">备案信息</h3>
          <el-divider />
          <ul>
            <li><a href="#">2024测试备案</a></li>
            <li><a href="#">苏ICP备000000号</a></li>
          </ul>
        </div>
      </div>
    </footer>

  </div>

</template>

<script lang="ts" setup>

import { ref } from 'vue'
const intro_image ="/src/assets/test-result.png"
const activeIndex = ref('1-2')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const activeNames = ref(['1'])

</script>

<style>
.flex-grow {
  flex-grow: 1;
}
.SearchResult-main{

  /* 背景设置为覆盖整个容器 */
  justify-content: center;
  min-width: 1080px;
  margin-left: 10%;
  min-height: 550px;
  background-size: cover;
  background-position: center;
}
.collapse{
  width: 600px;
  margin-left: 50px;
 }
/* 自定义折叠面板容器的背景色 */
.custom-collapse {
  background-color: #f0f0f0; /* 设置折叠面板容器的背景色 */
}
.custom-collapse .el-collapse-item__header {
  padding: 10px; /* 调整标题文本的内边距，使其与边框之间有一定间距 */
}
/* 可以根据需要设置折叠面板标题和内容的样式 */
.custom-collapse .el-collapse-item__header {
  background-color: #3574ad; /* 设置折叠面板标题的背景色 */
  color: #fff; /* 设置折叠面板标题的文本颜色 */
}
.footer {
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
