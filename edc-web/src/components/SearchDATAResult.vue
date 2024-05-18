<template>
  <div  class="common-layout" >
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
            <el-menu-item index="1-1" style="justify-content: center;" @click="router.push('/SearchAOP')">
              <p style="justify-content: center;" >EDC-AOP</p>
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
      <p style="font-size: 25px;
                  margin-left: 11%;
                  font-weight: bold;
                  justify-content: left;
                  display: flex;
                  color: #1B497BFF;">
        Research result
      </p>
      <el-divider style="margin-top: -5px;min-width: 1080px" />
      <el-main class="SearchResult-main" v-if="dataLoaded">
        <el-row :gutter="20" style="margin-left: 10%">
          <el-col :span="8">
            <el-card style="max-width: 480px;">
            <template #header>
              <div class="card-header">
                <span style="display: flex;justify-content: center">
                  <strong style="margin-right: 5px;">Name: </strong> {{ Data[0].name ? Data[0].name : 'null' }}
                </span>
              </div>
            </template>
<!--              <div style="display: flex;justify-content: center">-->
<!--                <el-image style="width: 350px;" :src=intro_image  />-->
<!--              </div>-->
              <div id="structure" ref="structure" v-html="svgContent"
                   style="display: flex;justify-content: center"></div>
            <template #footer >
              <span style="display: flex;justify-content: center">
                <strong style="margin-right: 5px;">Cas: </strong> {{ Data[0].cas ? Data[0].cas : 'null' }}
              </span>
            </template>
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
            ID:#{{ Data[0].id }}
          </p>
              <div class="collapse">
                <el-collapse v-model="activeNames" class="custom-collapse">
                  <el-collapse-item title="Smiles" name="Smiles" title-class="custom-title">
                    <div class="my_intro" style="">
                        {{ Data[0].smiles }}
                    </div>
                  </el-collapse-item>
                  <el-collapse-item title="Cas and Name" name="Cas">
                    <div class="my_intro" style="">
                      <el-space direction="vertical">
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">Cas:</strong> {{ Data[0].cas }}
                        </el-row>
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">Name:</strong> {{ Data[0].name ? Data[0].name : 'null' }}
                        </el-row>
                      </el-space>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item title="Endpoint, Assay and Source" name="endpoint">
                    <div class="my_intro" style="">
                      <el-space direction="vertical">
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">Endpoint:</strong> {{ Data[0].endpoint }}
                        </el-row>
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">Assay:</strong> {{ Data[0].assay }}
                        </el-row>
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">Source:</strong> {{ Data[0].source }}
                        </el-row>
                      </el-space>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item title="Other parameters" name="activity">
                    <div class="my_intro" style="">
                      <el-space direction="vertical">
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">Activity:</strong> {{ Data[0].activity }}
                        </el-row>
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">NOAEL:</strong> {{ Data[0].noael ? Data[0].noael : 'null' }}
                        </el-row>
                        <el-row :gutter="20">
                          <strong style="margin-right: 5px;">Unit:</strong> {{ Data[0].unit ? Data[0].unit : 'null' }}
                        </el-row>
                      </el-space>
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

<script  setup>

import {onMounted, ref} from 'vue'
import router from '../router'

import { useRoute } from 'vue-router';
import axios from "axios";
const Data = ref([]);
const route = useRoute();
const loading = ref(true); // 用于控制加载状态
const dataId = route.params.dataId;
const dataLoaded = ref(false);
const activeIndex = ref('1-2');
const structureElement = ref(null);
const activeNames = ref(['Smiles']);
const svgContent = ref('');
const renderStructure = () => {
  if (window.RDKit && Data.value.length > 0 && Data.value[0].smiles) {
    try {
      const mol = window.RDKit.get_mol(Data.value[0].smiles); // 确保 Data.value[0].smiles 是有效的 SMILES 字符串
      const svg = mol.get_svg();
      svgContent.value = svg;
      if (structureElement.value) {
        structureElement.value.innerHTML = svg;
      }
    } catch (error) {
      console.error('Error rendering structure:', error);
      if (structureElement.value) {
        structureElement.value.innerHTML = '<p>Invalid SMILES string</p>';
      }
    }
  } else {
    if (structureElement.value) {
      structureElement.value.innerHTML = '<p>No SMILES data available</p>';
    }
  }
};

onMounted(async () => {
  try {
    const response = await axios.get(`/findID?ID=${dataId}`);
    Data.value=response.data;
    dataLoaded.value = true; // 标志数据加载完成
  } catch (error) {
    console.error('获取数据失败:', error);
  } finally {
    loading.value=false
    console.info("data",Data)
    structureElement.value = document.getElementById('structure');
    // 在数据加载完成后直接渲染化学结构图
    renderStructure();
  }
});
</script>

<style>
.my_intro {
  word-wrap: break-word;
  margin-top: 20px;
  font-family: Arial;
  font-size: 16px;
  text-align: center;
}
.flex-grow {
  flex-grow: 1;
}
.SearchResult-main{
  background-image: url('../assets/background4.png');
  justify-content: center;
  min-width: 1080px;
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
  font-size: 16px;
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
