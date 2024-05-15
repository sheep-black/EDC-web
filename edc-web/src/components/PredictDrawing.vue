<template>
  <div>
    <div ref="sketcher" style="width: 500px; height: 300px;"></div>
  </div>
</template>

<script>
// 导入 ChemDoodle Web 组件的 JavaScript 文件
import './install/uis/jquery-ui-1.11.4.css';
import './install/ChemDoodleWeb.css';


export default {
  mounted() {
    // 在组件挂载后，执行 ChemDoodle Web 组件的初始化代码
    this.initChemDoodle();
  },
  methods: {
    initChemDoodle() {
      // 设置元素颜色
      ChemDoodle.ELEMENT['H'].jmolColor = 'black';
      ChemDoodle.ELEMENT['S'].jmolColor = '#B9A130';

      // 创建 ChemDoodle 组件实例
      const sketcher = new ChemDoodle.SketcherCanvas(this.$refs.sketcher, 500, 300, { useServices: true });

      // 设置样式和设置
      sketcher.styles.atoms_displayTerminalCarbonLabels_2D = true;
      sketcher.styles.atoms_useJMOLColors = true;
      sketcher.styles.bonds_clearOverlaps_2D = true;
      sketcher.styles.shapes_color = '#c10000';

      // 重新绘制
      sketcher.repaint();
    }
  }
};
</script>
