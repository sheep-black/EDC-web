<template>
  <div  class="common-layout" >
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="SearchDataResult-main" v-if="dataLoaded">
        <p style="font-size: 25px;
                  padding-right: 850px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;">
          Search result
          <el-link
              type="warning"
              :icon="ArrowLeftBold"
              style="justify-content: center;display: flex;right: -700px; font-size: 20px"
              @click="router.push('/SearchDATA')">
            Back to Search
          </el-link>
        </p>
        <el-divider style="margin-top: -5px;min-width: 1080px" />
        <el-row :gutter="10" style="margin-left: 10%">
          <el-col :span="10" style="display: flex;align-items: center;justify-content: right;padding-right: 100px">
            <el-card style="width: 500px;">
              <template #header>
                <div class="card-header">
                <span style="display: flex;justify-content: center">
                  <strong style="margin-right: 5px;">Name: </strong> {{ Data[0].name ? Data[0].name : 'null' }}
                </span>
                </div>
              </template>
              <div id="structure" ref="structure" v-html="svgContent"
                   style="display: flex;justify-content: center;height: 100%"></div>
              <template #footer >
              <span style="display: flex;justify-content: center">
                <strong style="margin-right: 5px;">CAS: </strong> {{ Data[0].cas ? Data[0].cas : 'null' }}
              </span>
              </template>
            </el-card>
          </el-col>
          <el-col :span="14" >
            <p style="font-size: 30px;
                  margin-left: 50px;
                  font-weight: bold;
                  margin-top: -5px;
                  justify-content: left;
                  display: flex;
                  color: #1e1a1a;">
              ID:#{{ Data[0].id }}
            </p>
            <div class="collapse-DATA">
              <el-collapse v-model="activeNames" class="custom-collapse" accordion style="width: 500px">
                <el-collapse-item title="Smiles" name="Smiles" title-class="custom-title">
                  <div class="my_intro" style="">
                    {{ Data[0].smiles }}
                  </div>
                </el-collapse-item>
                <el-collapse-item title="CAS and Name" name="CAS">
                  <div class="my_intro" style="">
                    <el-space direction="vertical" style="align-items: baseline">
                      <el-row >
                        <strong >CAS:</strong> {{ Data[0].cas }}
                      </el-row>
                      <el-row >
                        <strong>Name:</strong> {{ Data[0].name ? Data[0].name : 'null' }}
                      </el-row>
                    </el-space>
                  </div>
                </el-collapse-item>
                <el-collapse-item title="Endpoint, Assay and Source" name="endpoint">
                  <div class="my_intro" style="">
                    <el-space direction="vertical" style="align-items: baseline">
                      <el-row>
                        <strong>Endpoint:</strong> {{ Data[0].endpoint }}
                      </el-row>
                      <el-row>
                        <strong >Assay:</strong> {{ Data[0].assay }}
                      </el-row>
                      <el-row>
                        <strong>Source:</strong> {{ Data[0].source }}
                      </el-row>
                    </el-space>
                  </div>
                </el-collapse-item>
                <el-collapse-item title="Other parameters" name="activity">
                  <div class="my_intro" style="">
                    <el-space direction="vertical" style="align-items: baseline">
                      <el-row>
                        <strong>Activity:</strong> {{ Data[0].activity }}
                      </el-row>
                      <el-row>
                        <strong>NOAEL:</strong> {{ Data[0].noael ? Data[0].noael : 'null' }}
                      </el-row>
                      <el-row>
                        <strong>Unit:</strong> {{ Data[0].unit ? Data[0].unit : 'null' }}
                      </el-row>
                    </el-space>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </div>
          </el-col>
        </el-row>

      </el-main>
    </div>
    <FooterNav />

  </div>

</template>

<script  setup>

import {onMounted, ref} from 'vue'
import router from '../../router/index.js'

import { useRoute } from 'vue-router';
import axios from "axios";
import {ArrowLeftBold} from "@element-plus/icons-vue";
import HeaderNav from "@/components/HeaderNav.vue";
import FooterNav from "@/components/FooterNav.vue";
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

<style scoped>
:deep(.custom-collapse .el-collapse-item__header){
  height: 70px;
  background-color: #939292; /* 设置折叠面板标题的背景色 */
  color: #fff; /* 设置折叠面板标题的文本颜色 */
  font-size: 16px;
  padding: 10px; /* 调整标题文本的内边距，使其与边框之间有一定间距 */
}

</style>

<style>
.my_intro {
  word-wrap: break-word;
  margin-top: 20px;
  font-family: Arial;
  padding: 4%;
  font-size: 16px;
  text-align: left;
}
.flex-grow {
  flex-grow: 1;
}
.SearchDataResult-main{
  /* 设置图片作为背景 */
  background-image: url('../../assets/back-none.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height:80vh;
  background-size: cover;
  background-position: center;
  /* //height: 500px; 根据需要设置高度 */
}
.collapse-DATA{
  width: 80%;
  margin-left: 20px;
 }
/* 自定义折叠面板容器的背景色 */
.custom-collapse {
  width: 700px;
  background-color: #f0f0f0; /* 设置折叠面板容器的背景色 */
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
