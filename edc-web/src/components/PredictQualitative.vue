<template>
   <el-row :gutter="20"
           style="display: flex;justify-content: center; /* 水平居中 */align-items: center; /* 垂直居中 */"
           v-loading="loading"
           element-loading-text="Waiting in Line...">
          <el-col :span="12" style="display: flex;justify-content: center;align-items: center;">
            <el-card style="width: 80%;height: 100%">
              <template #header>
                <div style="height: 15px">
                  <strong >Input Smiles: </strong> {{ smiles }}
                </div>
              </template>
              <div ref="cyContainer" style="width: 100%;height:450px;"></div>
              <template #footer>
                <strong>
                  Prediction Result:
                </strong>
              </template>
            </el-card>
          </el-col>
          <el-col :span="12" >
            <el-card style="width: 80%;height: 100%">
              <template #header>
                <div class="card-header">
                  <span>Information</span>
                </div>
              </template>
              <div>
                <p>
                  <strong>
                    Node_type:
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
              </div>

              <template #footer>Footer content</template>
            </el-card>
          </el-col>
   </el-row>
</template>

<script  setup>

import {onMounted, ref} from 'vue'
import router from '../router'

import {useRoute} from 'vue-router';
import axios from "axios";
import cytoscape from "cytoscape";

const AOP_Data = ref([]);
const route = useRoute();
const loading = ref(true); // 用于控制加载状态
const smiles = route.params.smiles;
const cyContainer = ref(null);
const elements = ref([]);
const Node_Act=ref('0');
const cySucess=ref(true)
const terminalNodes = ref(new Set());
const startNodes = ref(new Set());
const clickNode = ref(null);
// 根据 WOE 值获取边的宽度
const getEdgeWidth = (WOE) => {
  switch (WOE) {
    case 'high':
      return 12;
    case 'moderate':
      return 6;
    case 'low':
      return 2;
    default:
      return 4;
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

    // 增加出度和入度
    outgoingEdges.set(row.source, outgoingEdges.get(row.source) + 1);
    incomingEdges.set(row.target, incomingEdges.get(row.target) + 1);
  });

  // 找出所有“终点”节点
  outgoingEdges.forEach((outDegree, node) => {
    if (outDegree === 0) {
      terminalNodes.value.add(node);
    }
  });

  // 找出所有“起始点”节点
  incomingEdges.forEach((inDegree, node) => {
    if (inDegree === 0) {
      startNodes.value.add(node);
    }
  });

  // 将终点节点的 type 属性设置为 "terminal"
  terminalNodes.value.forEach(id => {
    const node = nodes.find(n => n.data.id === id);
    if (node) {
      node.data.type = 'AO';
    }
  });

  // 将起始点节点的 type 属性设置为 "start"
  startNodes.value.forEach(id => {
    const node = nodes.find(n => n.data.id === id);
    if (node) {
      node.data.type = 'MIE';
    }
  });
  // 将既不是终点也不是起点的节点的 type 属性设置为 "KE"
  nodes.forEach(node => {
    if (!terminalNodes.value.has(node.data.id) && !startNodes.value.has(node.data.id)) {
      node.data.type = 'KE';
    }
  });
  // 根据 Node_Act 设置节点的活性
  nodes.forEach(node => {
    const activityStatus = Node_Act.value[node.data.id];
    // console.info("activityStatus",activityStatus);
    node.data.activity = activityStatus === 0 ? 'inactive' : 'active';
    node.data.actColor = activityStatus === 0 ? '#2c2c2c' : '#ff1616';
  });

  elements.value = [...nodes, ...edges];
  console.info("ele",elements)
};

onMounted(async () => {
  try {
    const predictresponse = await axios.get(`/PredictDX?input=${smiles}`);
    // 解析 result 字符串为对象
    const resultObject = JSON.parse(predictresponse.data.result);
    console.info("resultObject",resultObject.AOP);
    const response = await axios.get(`/getPredictAOP`);
    console.info("response.data",response.data);
    AOP_Data.value = resultObject.AOP;
    Node_Act.value = resultObject.endpoints;
    loading.value = false;
    fetchData();
    const cy = cytoscape({
      container: cyContainer.value, // 使用 ref 引用的容器
      elements: elements.value,
      style: [
        {
          selector: 'node[type="AO"]', // 选择 type 为 AO且活性 的节点
          style: {
            'shape': 'ellipse',
            'background-color': '#73cfff',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': 'data(actColor)',
            'font-size': '30px',
            'font-weight': 'bold',
            'width': '200px',
            'height': '100px',
            'text-wrap': 'wrap',
            'text-max-width': '140px',
            'border-width': '8px',
            'border-color': 'data(actColor)', // 根据活性设置边框颜色
          }
        },
        {
          selector: 'node[type="MIE"]', // 选择 type 为 start 的节点
          style: {
            'shape': 'diamond',  //
            'background-color': '#f5d6ff',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': 'data(actColor)',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '320px',
            'height': '150px',
            'text-wrap': 'wrap',
            'text-max-width': '120px', // 限制文本最大宽度
            'border-width': '8px',  // 设置边框宽度
            'border-color': 'data(actColor)', // 根据活性设置边框颜色
            'z-index': 10 // 确保边在节点之上
          }
        },
        {
          selector: 'node[type="KE"]', // 选择 type 为 KE 的节点
          style: {
            'background-color': '#7eabd2',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': 'data(actColor)',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '180px',
            'height': '100px',
            'shape': 'round-rectangle', // 设置为圆角矩形
            'text-wrap': 'wrap',
            'text-max-width': '140px', // 限制文本最大宽度
            'border-width': '8px',  // 设置边框宽度
            'border-color': 'data(actColor)', // 根据活性设置边框颜色
            'border-radius': '20px', // 设置圆角半径
          }
        },
        {
          selector: 'edge',
          style: {
            'width': 'data(width)',
            'line-color': '#b6b6b6',
            'target-arrow-shape': 'triangle', // 添加箭头
            'target-arrow-color': '#262626', // 箭头颜色
            'curve-style': 'bezier', // 使用贝塞尔曲线
            'opacity': 'data(value)' // 设置边的透明度为 50%
          }
        }
      ],
      wheelSensitivity: 0.2 // 调整滚轮缩放的灵敏度
    });
    // 添加节点点击事件监听器
    cy.on('tap', 'node', function(evt){
      clickNode.value = evt.target;
      console.log('Node clicked:', clickNode.value.data());
    });
// 布局终点节点
    const terminalNodes = cy.nodes('[type="AO"]');
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
        console.log('圆心坐标:', { centerX, centerY });
      }
    }).run();

// 布局中间节点和起始节点
    const nonTerminalNodes = cy.nodes('[type != "AO"]');
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
  } finally {
    console.info("data", AOP_Data.value);
  }
});

</script>

<style>
.el-loading-spinner .el-loading-text {
  color: var(--el-color-primary);
  font-size: 14px;
  margin: 3px 0;
}


</style>
