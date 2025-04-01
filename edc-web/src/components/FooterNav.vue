<!-- src/components/FooterNav.vue -->
<template>
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
        <p style="color: #ffffff;">E-mail：njutanhaoyue@nju.edu.cn & njushiwei@nju.edu.cn</p>
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
</template>

<script lang="ts" setup>
// 这里可以添加任何需要的逻辑
</script>

<style scoped>
.footer {
  min-width: 1080px;
  margin-left: -8px;
  margin-right: -8px;
  margin-bottom: -8px;
  background-color: #4d4d50;
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
