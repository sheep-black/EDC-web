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
              {{ clickNode ? clickNode.data().activity : ' ' }} (mg/kg bw/day)
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
              Sensitive AO :  {{ sEvent}}({{sEventName}})
            </strong>
          </template>
        </el-card>
      </el-col>
    </el-row>
    <el-divider />
      <p style="font-size: 25px;
                  padding-left: 13%;
                  font-weight: bold;
                  justify-content: left;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;">
        qAOP Information
        <el-popover
            placement="right-start"
            title="Download"
            :width="300"
            trigger="hover"
            content="Click to download the event related information file">
          <template #reference>
            <el-link :underline="false">
              <el-icon style="font-size: 20px;margin-top:10px;padding-left: 10px" @click="EventInfoDownload('event_info.xlsx')">
                <InfoFilled />
              </el-icon>
            </el-link>

          </template>
        </el-popover>
      </p>



    <div style="justify-content: center;display: flex;">
      <el-table
          :data="paginatedData"
          stripe
          border
          style="width:80%"
          :header-cell-style="{ background: '#dedede', color: '#000' }"
          :row-class-name="rowClassName">
        <el-table-column prop="AOP_id" label="AOP ID" width="100"></el-table-column>
        <el-table-column prop="events" label="Events" width="450"></el-table-column>
        <el-table-column prop="AOP_value" label="NOAELs for events"></el-table-column>
        <el-table-column label="Q Value">
          <template v-slot:header>
            <div style="display: flex; align-items: center; cursor: pointer;" @click="toggleSort">
              <span style="margin-right: 10px;">NOAEL (AO)</span>
              <span>
            <span v-if="sortOrder === 'asc'">↑</span>
            <span v-else>↓</span>
          </span>
            </div>
          </template>
          <template v-slot="scope">
            {{ scope.row.qValue }}
          </template>
        </el-table-column>
        <el-table-column label="Sensitive">
          <template v-slot:header>
            <div style="display: flex; align-items: center;">
              <span style="margin-right: 10px;">Sensitive</span>
              <el-select v-model="sensitiveFilter" placeholder="Select" size="small" style="width: 90px;">
                <el-option label="All" :value="null"></el-option>
                <el-option label="True" :value="true"></el-option>
                <el-option label="False" :value="false"></el-option>
              </el-select>
            </div>
          </template>
          <template v-slot="scope">
        <span :style="{ color: scope.row.sensitive ? 'green' : 'red' }">
          {{ scope.row.sensitive }}
        </span>
          </template>
        </el-table-column>
      </el-table>

    </div>
    <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="filteredTableData.length"
        layout="prev, pager, next, jumper"
        style="justify-content: center;display: flex;margin-top: 20px;"
    />
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
import router from '../router'

import {useRoute} from 'vue-router';
import axios from "axios";
import cytoscape from "cytoscape";
import {ElMessage} from "element-plus";
import {ArrowLeftBold, Document, InfoFilled} from "@element-plus/icons-vue";
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
const qAOP=ref({});
const tableData=ref([]);
const cy = ref(null); // 用于存储 Cytoscape 实例
const drawer=ref(false)
const sEvent=ref('');
const sEventName=ref('');
const sAOP=ref('');
const percentage = ref(10); // 初始进度百分比
const progressText = ref('Processing Smiles...'); // 初始进度文本
const sortOrder = ref('asc'); // 默认排序方式
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
const EventInfoDownload = async (filename) => {
  const url =`/download?fileName=${filename}`;
  try {
    const response = await axios.get(url, { responseType: 'blob' });
    const downloadUrl = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = downloadUrl;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    link.remove();
  } catch (error) {
    console.error('Error downloading file:', error);
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

const currentPage = ref(1);
const sensitiveFilter = ref(true); // 用于存储筛选条件
const pageSize = ref(10);
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
  const PreType = 'DL'; // 设置当前预测类型
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
  if(ifAD==='AD') {
    const PreType = 'DL'; // 设置当前预测类型
    const getImageUrl = `/getEventImage?PreType=${PreType}&smiles=${encodedSmiles}&id=${id}`;
    const response = await axios.get(getImageUrl, {
      responseType: 'blob', // 指定响应类型为 blob
    });
    console.info("response", response)
    // 创建一个 URL 对象来引用 blob 数据
    const url = URL.createObjectURL(response.data);
    eventImageSrc.value = url; // 设置图片源
  }
};
// 处理当前页变化
const handleCurrentChange = (page) => {
  currentPage.value = page;
};

// 转换数据格式并设置元素
const processqAOPData = () => {
  tableData.value = Object.keys(qAOP.value).map(key => {
    const aopItem = qAOP.value[key];
    return {
      AOP_id: key,
      AOP_value: aopItem.values.join(', '), // 将值数组转换为字符串
      events: aopItem.events ? aopItem.events.join('->') : 'N/A', // 使用 '->' 作为分隔符
      qValue: aopItem.qValue || 'N/A', // 如果没有 qValue，显示 'N/A'
      sensitive: aopItem.sensitive
    };
  });
};

// 切换排序
const toggleSort = () => {
  sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
};

// 计算筛选后的数据
const filteredTableData = computed(() => {
  let filteredData = tableData.value;
  if (sensitiveFilter.value !== null) {
    filteredData = filteredData.filter(item => item.sensitive === sensitiveFilter.value);
  }
  // 按 qValue 排序
  return filteredData.sort((a, b) => {
    const qValueA = typeof a.qValue === 'number' ? a.qValue : Number.NEGATIVE_INFINITY;
    const qValueB = typeof b.qValue === 'number' ? b.qValue : Number.NEGATIVE_INFINITY;
    return sortOrder.value === 'asc' ? qValueA - qValueB : qValueB - qValueA; // 根据当前排序方式排序
  });
});

// 计算分页数据
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredTableData.value.slice(start, end);
});

const rowClassName = (row) => {
  // 检查当前行的 AOP_id 是否与指定的 sAOP 值相等
  if (row.row.AOP_id === sAOP.value) {
    // 如果相等，返回高亮样式类名
    return 'success-row';
  } else {
    // 如果不相等，返回空字符串，表示不使用任何特殊样式
    return '';
  }
};

const fetchData = () => {
  const nodes = [];
  const edges = [];
  const nodeSet = new Set();
  const outgoingEdges = new Map(); // 记录每个节点的出度
  const incomingEdges = new Map(); // 记录每个节点的入度

  // 初始化节点
  AOP_Data.value.forEach(row => {
    if (!nodeSet.has(row.source)) {
      if (row.source !== "EDCs") { // 跳过 EDCs
        nodes.push({data: {id: row.source}});
        nodeSet.add(row.source);
        outgoingEdges.set(row.source, 0);
        incomingEdges.set(row.source, 0);
      }
    }
    if (!nodeSet.has(row.target)) {
      nodes.push({data: {id: row.target}});
      nodeSet.add(row.target);
      outgoingEdges.set(row.target, 0);
      incomingEdges.set(row.target, 0);
    }
  });

  // 根据 Node_Act 设置节点的活性
  nodes.forEach(node => {
    if (Node_Info.value[node.data.id] == null) {
      node.data.type = 'gray';
    } else {
      node.data.activity = Node_Info.value[node.data.id].Activity;
      node.data.moa = Node_Info.value[node.data.id].MOA;
      node.data.name = Node_Info.value[node.data.id].Name;
      node.data.nodetype = Node_Info.value[node.data.id]['Node-type'];
      node.data.type = Node_Info.value[node.data.id].Type;
    }

  });


  // 过滤掉 type 为 "gray" 的节点
  const filteredNodes = nodes.filter(node => node.data.type !== 'gray');

  // 重新遍历数据以添加边
  AOP_Data.value.forEach(row => {
    const sourceNode = filteredNodes.find(node => node.data.id === row.source);
    const targetNode = filteredNodes.find(node => node.data.id === row.target);

    // 检查源节点和目标节点是否存在于过滤后的节点中
    if (sourceNode && targetNode) {
      const width = getEdgeWidth(row.WOE);
      edges.push({
        data: {
          source: row.source,
          target: row.target,
          width: width
        }
      });
    }
  });

  elements.value = [...filteredNodes, ...edges];
  console.info("ele", elements);
};



onMounted(async () => {
  try {
    const progressValues = [10,30,50,70,90]; // 定义进度值
    let index = 0;
    const interval = setInterval(() => {
      if (index < progressValues.length) {
        percentage.value = progressValues[index];
        updateProgressText();
        index++;
      } else {
        clearInterval(interval); // 达到90%后停止
      }
    }, 5000); // 更新一次
    const predictresponse = await axios.get(`/PredictDL?input=${encodedSmiles}&ifAD=${ifAD}`);
    // 解析 result 字符串为对象
    const resultObject = JSON.parse(predictresponse.data.result);
    console.info("resultObject", resultObject);
    AOP_Data.value = resultObject.localAOP;
    Node_Info.value = resultObject.info;
    sEvent.value=resultObject.sEvent;
    sEventName.value = Node_Info.value[sEvent.value].Name;
    sAOP.value=resultObject.sAOP;
    qAOP.value=resultObject.AOP;
    console.info("qAOP",qAOP.value);
    processqAOPData();
    console.info("tableData",tableData)
    fetchData();
    loading.value = false;

    // 使用 nextTick 确保 DOM 更新完成
    await nextTick();

    cy.value = cytoscape({
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
          selector: 'node[type="KE"]', // 选择 type 为 KE 的节点
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
          selector: `node[id="${sEvent.value}"]`, // 选择高亮节点
          style: {
            'background-color': '#ff8e8e',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#2c2c2c',  // 黑色文本
            'font-size': '30px', // 增大字体大小
            'font-weight': 'bold', // 加粗字体
            'width': '210px',
            'height': '170px',
            'shape': 'star', // 设置为star
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
            'line-style': function (ele) {
              const value = ele.data('width');
              return value === 1 ? 'dotted' :
                  value === 3 ? 'dashed' :
                      'solid'; // 默认实线
            },
            'dash-pattern': '10 1',
            'opacity': function (ele) {
              const width = ele.data('width');
              if (width === 1) {
                return 0.1;   // 透明度为 0
              } else if (width === 3) {
                return 0.3; // 透明度为 50%
              } else if (width === 6) {
                return 0.5;   // 透明度为 100%
              } else {
                return 0.5;   // 默认透明度
              }
            },
          }
        }

      ],
      wheelSensitivity: 0.2 // 调整滚轮缩放的灵敏度
    });

    cy.value.on('tap', 'node', function (evt) {
      clickNode.value = evt.target;
      console.log('Node clicked:', clickNode.value.data());
      getEventImage(clickNode.value.data().id)
    });
// 布局终点节点
    const terminalNodes = cy.value.nodes('[type="KE"]');
    let centerX = 0;
    let centerY = 0;
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
    const radius = 800; // 圆的半径
    nonTerminalNodes.layout({
      name: 'cose',
      fit: true, // 是否调整视口以适应图形
      padding: 30, // 调整视口时的填充量
      boundingBox: {x1: centerX - radius, y1: centerY - radius, w: 2 * radius, h: 2 * radius}, // 限制布局边界
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
      duration: 0,
    })

  } finally {
    // console.info("data", AOP_Data.value);

  }
});

</script>

<style>
.el-table .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-9);
}
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

.event-image {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 设置阴影 */
  border-radius: 8px; /* 可选：设置圆角 */
  object-fit: fill; /* 适应容器 */
}

</style>
