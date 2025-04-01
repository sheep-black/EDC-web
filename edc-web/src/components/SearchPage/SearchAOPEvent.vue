<template>
  <div className="SearchResult-main" v-if="dataLoaded">
    <div ref="cyContainer" style="width: 100%; height: 600px; border: 1px solid black;"></div>
  </div>
</template>

<script setup>
import {ref, onMounted, nextTick} from 'vue';
import axios from 'axios';
import cytoscape from 'cytoscape';
import {useRoute} from 'vue-router';

const Data = ref([]);
const route = useRoute();
const loading = ref(true); // 用于控制加载状态
const dataId = route.params.dataId;
const dataLoaded = ref(false);
const cyContainer = ref(null);
const cyInstance = ref(null); // Cytoscape 实例
const elements = ref([]);

// 解析函数
const parseGraphString = (graphString) => {
  const elements = [];
  const nodes = new Set();
  const edges = [];

  const parts = graphString.split(',');
  for (let i = 0; i < parts.length; i++) {
    const part = parts[i].trim();
    if (part === '->') {
      if (i > 0 && i < parts.length - 1) {
        const source = parts[i - 1].trim();
        const target = parts[i + 1].trim();
        edges.push({data: {source, target}});
      }
    } else {
      nodes.add(part);
    }
  }

  nodes.forEach(id => elements.push({data: {id}}));
  edges.forEach(edge => elements.push(edge));
  console.info("element", elements);
  return elements;
};

const initCytoscape = (elements) => {
  if (cyContainer.value) {
    cyInstance.value = cytoscape({
      container: cyContainer.value,
      elements: elements,
      style: [
        {
          selector: 'node',
          style: {
            'background-color': '#0074D9',
            'label': 'data(id)',
            'text-valign': 'center',
            'text-halign': 'center',
            'color': '#fff',
            'font-size': '14px',
            'width': '30px',
            'height': '30px'
          }
        },
        {
          selector: 'edge',
          style: {
            'width': 2,
            'line-color': '#0074D9',
            'target-arrow-color': '#0074D9',
            'target-arrow-shape': 'triangle',
            'curve-style': 'bezier'
          }
        }
      ],
      layout: {
        name: 'grid'
      }
    });
  } else {
    console.error('cyContainer is null');
  }
};

onMounted(async () => {
  try {
    const response = await axios.get(`/findAOPID?ID=${dataId}`);
    Data.value = response.data;
    console.info("Data.value", Data.value[0].aops);
    elements.value = parseGraphString(Data.value[0].aops);
    dataLoaded.value = true; // 标志数据加载完成
    await nextTick(); // 确保 DOM 元素已被渲染
    initCytoscape(elements.value);
  } catch (error) {
    console.error('获取数据失败:', error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.SearchResult-main {
  padding: 20px;
}
</style>
