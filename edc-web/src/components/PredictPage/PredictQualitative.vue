<template>
  <!-- 加载提示文本 -->
  <div v-if="loading" style="display: flex;justify-content: center;align-items: center;">
    <el-space direction="vertical">
      <el-progress
          type="dashboard"
          :percentage="percentage"
          :color="currentColor"
          :duration="10"
          width="300"
          striped
          striped-flow
      />
      <div style="font-size: 18px;">
        <el-icon style="vertical-align: middle;" class="is-loading"><Loading /></el-icon>
        <span style="vertical-align: middle;"> Current Progress：{{ progressText }}, <strong>please don't leave this page</strong> </span>
      </div>
    </el-space>
  </div>
  <div v-else>
    <el-row :gutter="20"
            style="display: flex;justify-content: center; /* 水平居中 */align-items: center; /* 垂直居中 */">
      <el-col :span="12" style="display: flex;justify-content: center;align-items: center;">
        <el-card style="width: 80%;height: 100%">
          <template #header>
            <div style="position: relative; height: 15px;">
              <strong>Input Smiles: </strong> {{ smiles }}
              <el-popover
                  placement="right-start"
                  title="Tips"
                  :width="350"
                  trigger="hover"
                  content="Click on the node to view detailed information"
              >
                <template #reference>
                  <el-icon style="font-size: 20px; position: absolute; right: 0; top: 0; cursor: pointer;">
                    <InfoFilled />
                  </el-icon>
                </template>
              </el-popover>
            </div>
          </template>
          <div ref="cyContainer" style="width: 100%;height:450px;"></div>
          <template #footer>
            <div style="display: flex;justify-content: center; /* 水平居中 */">
              <el-button @click="saveAsPNG">Save As PNG</el-button>
              <el-button @click="resultExport('runs.json')">Save As JSON</el-button>
              <el-button @click="resultExport('runs.xlsx')">Export Xlsx</el-button>
            </div>
          </template>
        </el-card>
      </el-col>
      <el-col :span="12" >
        <el-card style="width: 80%;height: 100%">
          <template #header>
            <div style="position: relative;">
                  <span>
                    <strong>
                      Information
                    </strong>
                    <el-popover
                        placement="right-start"
                        title="About Node"
                        :width="300"
                        trigger="hover"
                        content="Detailed information of predicted nodes">
                      <template #reference>
                        <el-icon style="font-size: 20px; position: absolute; right: 0; top: 0; cursor: pointer;">
                          <InfoFilled />
                        </el-icon>
                      </template>
                    </el-popover>
                  </span>

            </div>
          </template>
          <div>
            <p>
              <strong>
                Name:
              </strong>
              {{ clickNode ? clickNode.data().name  : ' ' }}
            </p>
            <p>
              <strong>
                Type:
              </strong>
              {{ clickNode ? clickNode.data().type : ' ' }}
            </p>
            <p>
              <strong>
                Node_id:
              </strong>
              {{ clickNode ? clickNode.data().id : ' ' }}
            </p>
            <p>
              <strong>
                Activity:
              </strong>
              {{ clickNode ? clickNode.data().activity : ' ' }}
            </p>
            <p>
              <strong>
                MOA:
              </strong>
              {{ clickNode ? clickNode.data().moa  : ' ' }}
            </p>
            <p>
              <strong>
                Node-Type:
              </strong>
              {{ clickNode ? clickNode.data().nodetype  : ' ' }}
            </p>
            <el-divider v-if="ifAD === 'AD'">
              <p>Application domain (image)</p>
            </el-divider>
            <div v-if="ifAD === 'AD'" style="display: grid; place-items: center; /* 水平和垂直居中 */">
              <el-image :src="eventImageSrc" :fit="'fill'" class="event-image" style="width: 300px; height: 300px;">
              </el-image>
            </div>
          </div>
          <template #footer>
            <strong style="display: grid;place-items: center; /* 水平和垂直居中 */">
              Predict Result :  {{ PreReslut }}
            </strong>
          </template>
        </el-card>
      </el-col>
    </el-row>
    <el-tooltip class="item" effect="dark" content="Click to view detailed introduction document" placement="left">
      <el-button
          type="info"
          :icon="Document"
          circle
          size="large"
          style="position: fixed; right: 15px; bottom: 20%;box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);font-size: 20px;"
          @click="openDrawer"
      />
    </el-tooltip>
    <el-drawer
        v-model="drawer"
        title="Introduction"
        size="400px"
    >
      <div>
        <p>
          <strong>
            Pink Diamond Shaped:
          </strong>
          <p>
            Node type is MIE
          </p>
        </p>
        <p>
          <strong>
            Light Blue Circular Shaped:
          </strong>
          <p>
            Node type is AO
          </p>
        </p>
        <p>
          <strong>
            Deep Blue Rectangle Shaped:
          </strong>
          <p>
            Node type is KE
          </p>
        </p>
        <p>
          <strong>
            Red Highlight:
          </strong>
          <p>
            The node is active
          </p>
        </p>
        <p>
          <strong>
            Black Solid Line:
          </strong>
          <p>
            Biological plausibility is High
          </p>
        </p>
        <p>
          <strong>
            Gray Dashed Line:
          </strong>
          <p>
            Biological plausibility is Moderate
          </p>
        </p>
      </div>
    </el-drawer>
  </div>

</template>

<script  setup>

import {computed, nextTick, onMounted, ref} from 'vue'
import router from '../../router/index.js'

import {useRoute} from 'vue-router';
import axios from "axios";
import cytoscape from "cytoscape";
import {ElMessage} from "element-plus";
import {Document} from "@element-plus/icons-vue";
const eventImageSrc=ref("")
const AOP_Data = ref([]);
const route = useRoute();
const loading = ref(true); // 用于控制加载状态
const smiles = route.params.smiles;
const ifAD = route.params.ifAD;
const encodedSmiles = encodeURIComponent(smiles);
const cyContainer = ref(null);
const elements = ref([]);
const Node_Info=ref('0');
const cySucess=ref(true)
const clickNode = ref(null);
const PreReslut=ref('');
const cy = ref(null); // 用于存储 Cytoscape 实例
const drawer=ref(false);
const percentage = ref(10); // 初始进度百分比
const progressText = ref('Processing Smiles...'); // 初始进度文本
// 定义颜色和文本
const colors = {
  10: '#FF4D4F', // 10% 的颜色
  30: '#ff6e4d', // 30% 的颜色
  50: '#FFBF00', // 50% 的颜色
  70: '#37ff00', // 70% 的颜色
  90: '#4CAF50'  // 90% 的颜色
};

// 计算当前颜色
const currentColor = computed(() => colors[percentage.value]);

// 更新进度文本
const updateProgressText = () => {
  if (percentage.value === 10) {
    progressText.value = 'Processing Smiles...';
  } else if (percentage.value === 50) {
    progressText.value = 'Generating AD Image...';
  } else if (percentage.value === 90) {
    progressText.value = 'Model Prediction In Progress...';
  }
};


const getEdgeWidth = (WOE) => {
  switch (WOE) {
    case 'high':
      return 6;
    case 'moderate':
      return 3;
    case 'low':
      return 1;
    default:
      return 1;
  }
};
const openDrawer = () => {
  drawer.value=true
};

const saveAsPNG = () => {
  const pngData = cy.value.png({quality:1,full:true,bg:'white' }); // 设置 scale 参数来提高分辨率
  const link = document.createElement('a');
  link.href = pngData; // 将 PNG 数据作为链接
  link.download = 'graph.png'; // 设置文件名
  document.body.appendChild(link);
  link.click(); // 触发下载
  document.body.removeChild(link); // 下载后移除链接
};
const resultExport = async (fileName) => {
  const PreType = 'DX'; // 设置当前预测类型
  const url = `/downloadPredict?PreType=${PreType}&smiles=${encodedSmiles}&fileName=${fileName}`;
  console.info("predictUrl",url)
  try {
    // 使用 axios 发送 GET 请求
    const response = await axios.get(url, { responseType: 'blob' });
    // 创建一个 Blob URL
    const downloadUrl = window.URL.createObjectURL(new Blob([response.data]));
    // 创建一个链接元素并触发下载
    const link = document.createElement('a');
    link.href = downloadUrl;
    link.setAttribute('download', fileName); // 设置下载文件名
    document.body.appendChild(link);
    link.click(); // 触发下载
    link.remove(); // 移除链接元素
    // 释放 Blob URL
    window.URL.revokeObjectURL(downloadUrl);
  } catch (error) {
    console.error('Error downloading file:', error);
    // 这里可以添加错误处理逻辑，比如提示用户
  }

};
const getEventImage=async (id) => {
  if(ifAD==='AD'){
    const PreType = 'DX'; // 设置当前预测类型
    const getImageUrl = `/getEventImage?PreType=${PreType}&smiles=${encodedSmiles}&id=${id}`;
    const response = await axios.get(getImageUrl, {
      responseType: 'blob', // 指定响应类型为 blob
    });
    console.info("response",response)
    // 创建一个 URL 对象来引用 blob 数据
    const url = URL.createObjectURL(response.data);
    eventImageSrc.value = url; // 设置图片源
  }
};
// 转换数据格式并设置元素
const fetchData = () => {
  const nodes = [];
  const edges = [];
  const nodeSet = new Set();
  const outgoingEdges = new Map(); // 记录每个节点的出度
  const incomingEdges = new Map(); // 记录每个节点的入度

  // 初始化节点和边
  AOP_Data.value.forEach(row => {
    if (!nodeSet.has(row.source)) {
      if(row.source !== "EDCs"){//跳过EDCs
        nodes.push({ data: { id: row.source } });
        nodeSet.add(row.source);
        outgoingEdges.set(row.source, 0);
        incomingEdges.set(row.source, 0);
      }

    }
    if (!nodeSet.has(row.target)) {
      nodes.push({ data: { id: row.target } });
      nodeSet.add(row.target);
      outgoingEdges.set(row.target, 0);
      incomingEdges.set(row.target, 0);
    }
    const width = getEdgeWidth(row.WOE);
    if(row.source !== "EDCs"){
      edges.push({
        data: {
          value: row.value,
          source: row.source,
          target: row.target,
          width: width
        }
      });
    }

  });

  // 根据 Node_Act 设置节点的活性
  nodes.forEach(node => {
    const activityStatus = Node_Info.value[node.data.id].Activity;
    node.data.activity = activityStatus === 0 ? 'inactive' : 'active';
    node.data.moa = Node_Info.value[node.data.id].MOA;
    node.data.name = Node_Info.value[node.data.id].Name;
    node.data.nodetype = Node_Info.value[node.data.id]['Node-type'];
    node.data.type = Node_Info.value[node.data.id].Type;
  });

  elements.value = [...nodes, ...edges];
  console.info("ele",elements)
};

onMounted(async () => {
  try {
    const progressValues = [10,30, 50,70, 90]; // 定义进度值
    let index = 0;
    const interval = setInterval(() => {
      if (index < progressValues.length) {
        percentage.value = progressValues[index];
        updateProgressText();
        index++;
      } else {
        clearInterval(interval); // 达到90%后停止
      }
    }, 10000); // 每秒更新一次
    const predictresponse = await axios.get(`/PredictDX?input=${encodedSmiles}&ifAD=${ifAD}`);
    const resultObject = JSON.parse(predictresponse.data.result);
    console.info("resultObject",resultObject);
    AOP_Data.value = resultObject.localAOP;
    Node_Info.value = resultObject.info;

    if(resultObject.pred===1){
      PreReslut.value='EDC'
    }else{
      PreReslut.value='no-EDC'
    }
    loading.value = false;
    fetchData();
    loading.value = false;

    // 使用 nextTick 确保 DOM 更新完成
    await nextTick();

    cy.value = cytoscape({
      container: cyContainer.value, // 使用 ref 引用的容器
      elements: elements.value,
      style: [
        {
          selector: 'node[type="AO"][activity="active"]', // 选择 type 为 AO且活性 的节点
          style: {
            'shape': 'ellipse',
            'background-color': '#ff8e8e',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#2c2c2c',
            'font-size': '30px',
            'font-weight': 'bold',
            'width': '200px',
            'height': '100px',
            'text-wrap': 'wrap',
            'text-max-width': '140px',
            'border-width': '8px',
            'border-color': '#2c2c2c',
          }
        },
        {
          selector: 'node[type="AO"][activity="inactive"]', // 选择 type 为 AO且活性 的节点
          style: {
            'shape': 'ellipse',
            'background-color': '#73cfff',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#2c2c2c',
            'font-size': '30px',
            'font-weight': 'bold',
            'width': '200px',
            'height': '100px',
            'text-wrap': 'wrap',
            'text-max-width': '140px',
            'border-width': '8px',
            'border-color': '#2c2c2c', // 根据活性设置边框颜色
          }
        },

        {
          selector: 'node[type="MIE"][activity="active"]', // 选择 type 为 start 的节点
          style: {
            'shape': 'diamond',  //
            'background-color': '#ff8e8e',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#2c2c2c',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '320px',
            'height': '150px',
            'text-wrap': 'wrap',
            'text-max-width': '120px', // 限制文本最大宽度
            'border-width': '8px',  // 设置边框宽度
            'border-color': '#2c2c2c',
          }
        },
        {
          selector: 'node[type="MIE"][activity="inactive"]', // 选择 type 为 start 的节点
          style: {
            'shape': 'diamond',  //
            'background-color': '#f5d6ff',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#2c2c2c',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '320px',
            'height': '150px',
            'text-wrap': 'wrap',
            'text-max-width': '120px', // 限制文本最大宽度
            'border-width': '8px',  // 设置边框宽度
            'border-color': '#2c2c2c',
          }
        },
        {
          selector: 'node[type="KE"][activity="active"]', // 选择 type 为 KE 的节点
          style: {
            'background-color': '#ff8e8e',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#2c2c2c',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '180px',
            'height': '100px',
            'shape': 'round-rectangle', // 设置为圆角矩形
            'text-wrap': 'wrap',
            'text-max-width': '140px', // 限制文本最大宽度
            'border-width': '8px',  // 设置边框宽度
            'border-color': '#2c2c2c',
            'border-radius': '20px', // 设置圆角半径
          }
        },
        {
          selector: 'node[type="KE"][activity="inactive"]', // 选择 type 为 KE 的节点
          style: {
            'background-color': '#7eabd2',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#2c2c2c',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '180px',
            'height': '100px',
            'shape': 'round-rectangle', // 设置为圆角矩形
            'text-wrap': 'wrap',
            'text-max-width': '140px', // 限制文本最大宽度
            'border-width': '8px',  // 设置边框宽度
            'border-color': '#2c2c2c',
            'border-radius': '20px', // 设置圆角半径
          }
        },
        {
          selector: 'edge',
          style: {
            'width': 'data(width)',
            'line-color': '#464646',
            'target-arrow-shape': 'triangle', // 添加箭头
            'target-arrow-color': '#424242', // 箭头颜色
            'curve-style': 'bezier', // 使用贝塞尔曲线
            'line-style': function(ele) {
              const value = ele.data('value');
              return value === 0 ? 'dotted' :
                  value === 0.5 ? 'dashed' :
                      'solid'; // 默认实线
            },
            'dash-pattern':'10 1',
            'opacity': 'data(value)',
          }
        }
      ],
      wheelSensitivity: 0.2 // 调整滚轮缩放的灵敏度
    });
    // console.info("cy",cy)
    // 添加节点点击事件监听器
    cy.value.on('tap', 'node', function(evt){
      clickNode.value = evt.target;
      console.log('Node clicked:', clickNode.value.data());
      getEventImage(clickNode.value.data().id)
    });
// 布局终点节点
    const terminalNodes = cy.value.nodes('[type="KE"]');
    let centerX=0;
    let centerY=0;
    terminalNodes.layout({
      name: 'circle',
      radius: 300, // 圆圈半径
      avoidOverlap: true, // 避免节点重叠
      animate: true, // 动画效果
      minNodeSpacing: 50, // 增加节点之间的最小间距
      spacingFactor: 0.8, // 增加间距因子
      animationDuration: 1000, // 动画持续时间
      ready: function () {
        // 布局完成后计算圆心坐标
        const boundingBox = terminalNodes.boundingBox();
        centerX = boundingBox.x1 + (boundingBox.w / 2);
        centerY = boundingBox.y1 + (boundingBox.h / 2);
      }
    }).run();

// 布局中间节点和起始节点
    const nonTerminalNodes = cy.value.nodes('[type != "KE"]');
    const radius = 1200; // 圆的半径
    nonTerminalNodes.layout({
      name: 'cose',
      fit: true, // 是否调整视口以适应图形
      padding: 30, // 调整视口时的填充量
      boundingBox: { x1: centerX - radius, y1: centerY - radius, w: 2 * radius, h: 2 * radius }, // 限制布局边界
      avoidOverlap: true, // 防止节点重叠
      nodeDimensionsIncludeLabels: false, // 计算节点边界框时不包含标签
      animate: true, // 是否启用动画
      animationDuration: 1000, // 动画持续时间
      animationEasing: undefined, // 动画的缓动函数
      ready: function () { // 布局准备好时的回调函数
      },
      stop: function () { // 布局停止时的回调函数
        // 布局完成后进行随机扰动
        nonTerminalNodes.forEach(node => {
          const position = node.position();
          const offsetX = (Math.random() - 0.5) * 300; // 随机偏移量，范围为 [-10, 10]
          const offsetY = (Math.random() - 0.5) * 300; // 随机偏移量，范围为 [-10, 10]
          node.position({
            x: position.x + offsetX,
            y: position.y + offsetY
          });
        });
      },
      transform: function (node, position) { // 转换给定节点位置的函数
        return position;
      },
      idealEdgeLength: 100, // 理想的边长
      nodeRepulsion: 4000, // 节点之间的排斥力
      edgeElasticity: 100, // 边的弹性
      nestingFactor: 0.1, // 节点嵌套因子
      gravity: 1, // 重力
      numIter: 1000, // 迭代次数
      initialTemp: 200, // 初始温度
      coolingFactor: 0.95, // 冷却因子
      minTemp: 1.0 // 最低温度
    }).run();
    cySucess.value = true; // 更新加载状态
  } catch (error) {
    console.error('获取数据失败:', error);
    ElMessage({
      showClose: true,
      message: 'Oops! Prediction error, please try again later.',
      type: 'error',
      duration:0,
    })
  } finally {
    // console.info("data", AOP_Data.value);

  }
});

</script>

<style>
.el-loading-spinner .el-loading-text {
  color: var(--el-color-primary);
  font-size: 14px;
  margin: 3px 0;
}
.el-carousel__item:nth-child(2n) {
  background-color: #c1d8f6;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #f3f3f3;
}
.event-image{
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 设置阴影 */
  border-radius: 8px; /* 可选：设置圆角 */
  object-fit: fill; /* 适应容器 */
}

</style>
