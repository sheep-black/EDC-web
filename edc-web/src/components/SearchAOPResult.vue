<template>
  <div  class="common-layout" >
    <el-container style="margin-top: -2px;">
      <el-header>
        <!--        头部菜单栏-->
        <el-menu
            :default-active="activeIndex"
            style="min-width: 1080px; margin-left: -25px;margin-right: -25px;margin-top: -5px"
            mode="horizontal"
            background-color="#3a3b3d"
            text-color="#fff"
            active-text-color="#ffcc66"
            :ellipsis="false"
        >
          <el-menu-item index="0" @click="router.push('/')">
            <!--      菜单左侧标志-->
            <img src="../assets/network.svg" style="height: 45px; width: 45px;" alt="Your Icon" />
            <p style="font-size: 18px;margin-left: 6px;font-weight: bold;text-shadow: 1px 1px 2px black;">
              EDCNDP.ai
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
            <el-menu-item index="1-3" style="justify-content: center;" @click="router.push('/SearchPredict')">
              <p style="justify-content: center;">EDC-GECs</p>
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

      <el-main class="SearchResult-main"  v-if="dataLoaded" >
        <p style="font-size: 25px;
                  padding-right: 900px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;">
          Search result
          <el-link
              type="warning"
              :icon="ArrowLeftBold"
              style="justify-content: center;display: flex;right: -800px; font-size: 20px"
              @click="router.push('/SearchAOP')">
            Back to Search
          </el-link>
        </p>
        <el-divider style="margin-top: -5px;min-width: 1080px" />
        <el-row :gutter="10" style="margin-left: 5%">
          <el-col :span="10" style="display: flex;align-items: center;justify-content: right;padding-right: 100px"  >
            <el-card style="width: 500px;">
              <template #header>
                <div class="card-header">
                <span style="display: flex;justify-content: center">
                  <strong style="margin-right: 5px;">pmid: </strong> {{ Data[0].pmid ? Data[0].pmid : 'null' }}
                </span>
                </div>
              </template>
              <div v-loading="cySucess">
                <div ref="cyContainer" style="width: 100%; height:400px; border: 1px solid black;"></div>
              </div>

            </el-card>
          </el-col>
          <el-col :span="14">
            <p style="font-size: 30px;
                  margin-left: 50px;
                  font-weight: bold;
                  margin-top: -5px;
                  justify-content: left;
                  display: flex;
                  color: #1e1a1a;">
              AOP_ID: #{{ Data[0].aop_ID }}
            </p>
            <div class="AOP_collapse">
              <el-collapse v-model="activeNames" class="custom-collapse">
                <el-collapse-item title="MIE" name="MIE">
                    <ul>
                      <li v-for="item in MIE" >

                        <strong >MIE:{{item.eventID.split('_')[1]}}——</strong> {{item.eventTitle}}
                      </li>
                    </ul>
                </el-collapse-item>
                <el-collapse-item title="KE" name="KE">
                  <ul>
                    <li v-for="item in KE" >
                      <strong >KE:{{item.eventID.split('_')[1]}}——</strong> {{item.eventTitle}}
                    </li>
                  </ul>
                </el-collapse-item>
                <el-collapse-item title="AO" name="AO">
                  <ul>
                    <li v-for="item in AO" >
                      <strong >AO:{{item.eventID.split('_')[1]}}——</strong> {{item.eventTitle}}
                    </li>
                  </ul>
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
          <p style="color: #ffffff;">Nanjing University Reserved</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;">Contact</h3>
          <el-divider />
          <p style="color: #ffffff;">E-mail：njutanhaoyue@nju.edu.cn</p>
          <p style="color: #ffffff;">Postcode：210023</p>
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

import {nextTick, onMounted, ref} from 'vue'
import router from '../router'

import { useRoute } from 'vue-router';
import axios from "axios";
import cytoscape from "cytoscape";
import {ArrowLeftBold} from "@element-plus/icons-vue";
const Data = ref([]);
const route = useRoute();
const loading = ref(true); // 用于控制加载状态
const dataId = route.params.dataId;
const dataLoaded = ref(false);
const cyContainer = ref(null);
const cyInstance = ref(null); // Cytoscape 实例
const elements = ref([]);
const MIE=ref([]);
const KE=ref([]);
const AO=ref([]);
const activeIndex = ref('1-1');
const activeNames=ref('MIE');
const cySucess=ref(true)
// 解析函数
const parseGraphString = async (graphString) => {
  const aops = [];
  const elements = []; // 用于存储节点和边的数组

  // 将输入的字符串按箭头 "->" 分割成多个组
  const groups = graphString.split('->');

  // 遍历每个组
  for (let index = 0; index < groups.length; index++) {
    const group = groups[index];
    const nodes = group.trim().split(','); // 将组内的节点按 "," 分割成数组

    // 添加节点
    for (let nodeIndex = 0; nodeIndex < nodes.length; nodeIndex++) {
      const node = nodes[nodeIndex];
      const trimmedNode = node.trim();
      const nodeData = { id: trimmedNode };
      if (index === 0) {
        nodeData.type = 'root'; // 根节点
      } else if (index === groups.length - 1) {
        nodeData.type = 'end'; // 末节点
      }else{
        nodeData.type = 'ke'; // 中间节点
      }
      elements.push({ data: nodeData }); // 添加节点

      // 发送 Axios 请求
      try {
        const response = await axios.get(`/searchEventAOP?eventID=${trimmedNode}`);
        if (nodeData.type === 'root') {
          let newItem = {
            eventID: trimmedNode,
            eventTitle: response.data.eventTitle
          }
          MIE.value.push(newItem);
        } else if (nodeData.type === 'end') {
          let newItem = {
            eventID: trimmedNode,
            eventTitle: response.data.eventTitle
          }
          AO.value.push(newItem);
        }else{
          let newItem = {
            eventID: trimmedNode,
            eventTitle: response.data.eventTitle
          }
          KE.value.push(newItem);
        }
        // 请求成功处理逻辑
        aops.push(...response.data.aops.split(','));
        // 使用 Set 来存储已经添加的边
        const edgeSet = new Set();
          // 添加 aop 节点和边
        for (const aop of aops) {
          const node = { id: aop, type: 'aop' };
          elements.push({ data: node }); // 添加aop节点
          // 构造边的唯一标识
          const edgeId = `${trimmedNode}-${aop}`;
          // 如果这条边不在 Set 中，说明是新边，可以添加
          if (!edgeSet.has(edgeId)) {
            elements.push({ data: { source: trimmedNode, target: aop, type: 'aop' } }); // 添加aop边
            // 将边的唯一标识加入 Set 中
            edgeSet.add(edgeId);
          }
        }

      } catch (error) {
        // 请求失败处理逻辑
        console.error(`Event ${trimmedNode} 请求失败:`, error);
      }
    }

    // 添加边
    if (index !== groups.length - 1) {
      const nextGroupNodes = groups[index + 1].trim().split(','); // 获取下一个组的节点

      for (const sourceNode of nodes) {
        for (const targetNode of nextGroupNodes) {
          elements.push({ data: { source: sourceNode.trim(), target: targetNode.trim(), type: 'main'} }); // 添加边
        }
      }
    }
  }


  return elements; // 返回元素数组
};

const initCytoscape = (elements) => {
  for (let i = 0; i < elements.length; i++) {
    const element = elements[i];
    if (element.data && element.data.type === 'root') {
      if (element.data.id.includes('wiki')) {
        // 如果id包含“wiki”，执行相应操作
        const nodeName=element.data.id.split('_')[1]+'(AOP-wiki)'

        element.data.label = `MIE:${nodeName}`;
      }else{
        const nodeName=element.data.id.split('_')[1]
        element.data.label = `MIE:${nodeName}`;
      }
    } else if (element.data && element.data.type === 'end') {
      if (element.data.id.includes('wiki')) {
        // 如果id包含“wiki”，执行相应操作
        const nodeName=element.data.id.split('_')[1]+'(AOP-wiki)'

        element.data.label = `AO:${nodeName}`;
      }else{
        const nodeName=element.data.id.split('_')[1]
        element.data.label = `AO:${nodeName}`;
      }
    } else if(element.data && element.data.type === 'ke'){
      if (element.data.id.includes('wiki')) {
        // 如果id包含“wiki”，执行相应操作
        const nodeName=element.data.id.split('_')[1]+'(AOP-wiki)'

        element.data.label = `KE:${nodeName}`;
      }else{
        const nodeName=element.data.id.split('_')[1]
        element.data.label = `KE:${nodeName}`;
      }
    }else{
      element.data.label = `AOP:${element.data.id}`;
    }
  }

  if (cyContainer.value) {
    cyContainer.value = cytoscape({
      container: cyContainer.value,
      elements: elements,
      style: [
        {
          selector: 'node[type="ke"]',
          style: {
            'background-color': '#7eabd2',
            'label': 'data(label)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#000000',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '300px',
            'height': '120px',
            'shape': 'round-rectangle', // 设置为圆角矩形
            'text-wrap': 'wrap',
            'text-max-width': '140px', // 限制文本最大宽度
            'border-width': '2px',  // 设置边框宽度
            'border-color': '#000000',  // 设置边框颜色
            'border-radius': '20px', // 设置圆角半径
            'z-index': 10 // 确保边在节点之上
          },
        },
        {
          selector: 'node[type="root"]',
          style: {
            'shape': 'diamond',  //
            'background-color': '#f5d6ff',
            'label': 'data(label)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#000000',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '320px',
            'height': '150px',
            'text-wrap': 'wrap',
            'text-max-width': '120px', // 限制文本最大宽度
            'border-width': '2px',  // 设置边框宽度
            'border-color': '#000000',  // 设置边框颜色
            'z-index': 10 // 确保边在节点之上
          }
        },
        {
          selector: 'node[type="aop"]',
          style: {
            'shape': 'ellipse',  // 改成圆形
            'background-color': '#bbbbbb',  // 淡蓝色背景
            'label': 'data(label)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#000000',  // 黑色文本
            'font-size': '18px', // 缩小字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '90px',  // 缩小宽度
            'height': '90px',  // 缩小高度
            'text-wrap': 'wrap',
            'text-max-width': '100px', // 缩小文本最大宽度
            'border-width': '2px',  // 设置边框宽度
            'border-color': '#000000',  // 设置边框颜色
            'z-index': 10 // 确保边在节点之上
          }
        },
        {
          selector: 'node[type="end"]',
          style: {
            'shape': 'ellipse',
            'background-color': '#73cfff',
            'label': 'data(label)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#000000',
            'font-size': '30px',
            'font-weight': 'bold',
            'width': '300px',
            'height': '120px',
            'text-wrap': 'wrap',
            'text-max-width': '140px',
            'border-width': '2px',
            'border-color': '#000000',
            'z-index': 1 // 确保边在节点之上
          }
        },
        {
          selector: 'edge[type="aop"]',
          style: {
            'width': 1,
            'line-color': '#797a7c',
            'curve-style': 'bezier'
          }
        },
        {
          selector:'edge[type="main"]',
          style: {
            'width': 8,
            'line-color': '#2a2a2a',
            'target-arrow-color': '#2a2a2a',
            'target-arrow-shape': 'triangle',
            'curve-style': 'bezier',
            'z-index': 9999 // 确保边在节点之上
          }
        },

      ],
      layout: {
        name: 'cose', // 使用 COSE 布局使图均匀分布
        animate: true,
        animationDuration: 1000,
        animationEasing: 'ease-in-out', // 动画缓动函数
        fit: true, // 调整视图以适应布局
        padding: 10, // 视图周围的填充
        nodeRepulsion:800000, // 增加节点间的排斥力
        idealEdgeLength: 200, // 增加理想的边长度
        edgeElasticity: 50, // 减少边的弹性
        nestingFactor: 5, // 缩放层次结构的系数
        gravity: 50, // 减少重力因子
        numIter: 1000, // 迭代次数
        initialTemp: 200, // 初始温度
        coolingFactor: 0.95, // 冷却因子
        minTemp: 1.0, // 最低温度
        nodeOverlap:20000,
      },
      wheelSensitivity: 0.1 // 调整滚轮缩放的灵敏度
    });
  } else {
    console.error('cyContainer is null');
  }
};

onMounted(async () => {
  try {
    const response = await axios.get(`/findAOPID?ID=${dataId}`);
    Data.value = response.data;

    dataLoaded.value = true; // 标志数据加载完成
    await nextTick(); // 确保 DOM 元素已被渲染
    // initEvent(Data.value[0].aops)
    parseGraphString(Data.value[0].aops).then(elements => {
      elements.value = elements
      cySucess.value=false;
      initCytoscape(elements.value);
      //监听双击：
      // cyContainer.value.on('dblclick', 'node', (event) => {
      //   const node = event.target;
      //   console.log('Node double clicked:', node.id());
      // });
    })
  } catch (error) {
    console.error('获取数据失败:', error);
  } finally {
    loading.value = false;
  }
});

</script>

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
.SearchResult-main{
  /* 设置图片作为背景 */
  background-image: url('../assets/back-none.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height: 80vh;
  background-size: cover;
  background-position: center;
  /* //height: 500px; 根据需要设置高度 */
}

/* 自定义折叠面板容器的背景色 */
.custom-collapse {
  background-color: #f0f0f0; /* 设置折叠面板容器的背景色 */
}
.custom-collapse .el-collapse-item__header {
  padding: 5%; /* 调整标题文本的内边距，使其与边框之间有一定间距 */
}
/* 可以根据需要设置折叠面板标题和内容的样式 */
.custom-collapse .el-collapse-item__header {
  background-color: #939292; /* 设置折叠面板标题的背景色 */
  color: #fff; /* 设置折叠面板标题的文本颜色 */
  font-size: 16px;
}
.custom-collapse .el-collapse-item__content {
  font-size: 14px; /* 设置折叠面板内部文字的大小 */
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
