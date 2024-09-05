<template>
   <el-row :gutter="20"
           style="display: flex;justify-content: center; /* 水平居中 */align-items: center; /* 垂直居中 */"
           v-loading="loading"
           element-loading-text="Waiting in Line...">
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
                  <el-button @click="resultExport('result.json')">Save As JSON</el-button>
                  <el-button @click="resultExport('result.xlsx')">Export Xlsx</el-button>
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
                <p>
                  <strong>
                    Node_name:
                  </strong>
                  {{ clickNode ? clickNode.data().id  : ' ' }}
                </p>
                <p>
                  <strong>
                    Node_name2:
                  </strong>
                  {{ clickNode ? clickNode.data().id  : ' ' }}
                </p>
                <p>
                  <strong>
                    Node_name3:
                  </strong>
                  {{ clickNode ? clickNode.data().id  : ' ' }}
                </p>
                <p>
                  <strong>
                    Image:
                  </strong>
                  {{ clickNode ? clickNode.data().id  : ' ' }}
                </p>
              </div>
              <template #footer>
                <strong>
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
</template>

<script  setup>

import {onMounted, ref} from 'vue'
import router from '../router'

import {useRoute} from 'vue-router';
import axios from "axios";
import cytoscape from "cytoscape";
import {ElMessage} from "element-plus";
import {Document} from "@element-plus/icons-vue";

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
const PreReslut=ref('');
const cysvg=ref(null);
const cy = ref(null); // 用于存储 Cytoscape 实例
const drawer=ref(false)
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
  const url = `/downloadPredict?PreType=${PreType}&smiles=${smiles}&fileName=${fileName}`;
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
  });

  elements.value = [...nodes, ...edges];
  console.info("ele",elements)
};

onMounted(async () => {
  try {
    const predictresponse = await axios.get(`/PredictDX?input=${smiles}`);
    // 解析 result 字符串为对象
    const resultObject = JSON.parse(predictresponse.data.result);
    console.info("resultObject",resultObject);
    AOP_Data.value = resultObject.AOP;
    Node_Act.value = resultObject.endpoints;
    console.info("Node_Act",Node_Act.value );

    if(resultObject.pred===1){
      PreReslut.value='EDC'
    }else{
      PreReslut.value='no-EDC'
    }
    loading.value = false;
    fetchData();
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
    console.info("cy",cy)
    // 添加节点点击事件监听器
    cy.value.on('tap', 'node', function(evt){
      clickNode.value = evt.target;
      // console.log('Node clicked:', clickNode.value.data());
    });
// 布局终点节点
    const terminalNodes = cy.value.nodes('[type="AO"]');
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
    const nonTerminalNodes = cy.value.nodes('[type != "AO"]');
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

</style>
