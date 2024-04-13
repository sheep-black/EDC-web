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
              <p style="justify-content: center;">EDC-AOP</p>
            </el-menu-item>
            <el-menu-item index="1-2" style="justify-content: center;">
              <p style="justify-content: center;">EDC-DATA</p>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon :size="20"><Odometer /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Predict
              </p>
            </template>
            <el-menu-item index="2-1" style="justify-content: center;">
              <p style="justify-content: center;">Qualitative predictor</p>
            </el-menu-item>
            <el-menu-item index="2-2" style="justify-content: center;">
              <p style="justify-content: center;">Quantitative predictor</p>
            </el-menu-item>
          </el-sub-menu>
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
      <el-main class="SearchAOP-main">
        <p style="font-size: 25px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1B497BFF;">
          EDC-AOP  Search
        </p><p style="font-size: 16px;
                  margin-top: -10px;
                  justify-content: center;
                  display: flex;
                  color: #000000;">
        从这里开始你的搜索描述abababababbaabab
      </p>

        <el-radio-group fill="#377ecb" v-model="selected"
                        style="font-weight: bold;justify-content: center;display: flex;">
          <el-radio-button label="TextSearch" value="TextSearch" />
          <el-radio-button label="NodeSearch" value="NodeSearch" />
          <el-radio-button label="EventSearch" value="EventSearch" />
        </el-radio-group>
        <el-divider />
        <component :is="selectedComponent" />

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


import { ref ,computed} from 'vue'
import SearchAOPText from "../components/SearchAOPText.vue";
import SearchAOPEvent from "../components/SearchAOPEvent.vue";
import SearchAOPNode from "../components/SearchAOPNode.vue";
const activeIndex = ref('1-1')
const selected = ref('TextSearch')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const selectedComponent = computed(() => {
  if (selected.value === 'TextSearch') {
    return <string>SearchAOPText;
  } else if (selected.value === 'EventSearch') {
    return <string>SearchAOPEvent;
  } else {
    return <string>SearchAOPNode;
  }
});
</script>

<style>
.flex-grow {
  flex-grow: 1;
}
.intro-text {
  /* 中间介绍字体的样式*/
  font-size: 18px;
  max-width: 1000px; /* 设置最大宽度，以避免文本过长 */
  text-align: justify; /* 文本两端对齐 */
  line-height: 200%; /* 设置行高 */
}
.SearchAOP-main{
  /* 设置图片作为背景 */
//background-image: url('../assets/home-background.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height: 450px;
  background-size: cover;
  background-position: center;
//height: 500px; /* 根据需要设置高度 */
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
