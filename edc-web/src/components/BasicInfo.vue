<script setup>
import router from '../router'

</script>

<template>
  <p style="font-size: 20px;
                  padding: 0 20px;
                  font-weight: bold;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 1px 1px 2px #ffcc66;">
    Basic Information
  </p>
  <el-divider border-style="dashed" />
  <div class="markdown">
    <h2>What is AOP?</h2>
    <p>
      Adverse Outcome Pathways (AOPs) are a conceptual framework for organizing, synthesizing, and presenting specialized scientific knowledge regarding the linkage between perturbation of a specific biological target, pathway or process by a stressor (e.g., a chemical, radiation, a virus), and a consequent adverse outcome(s) considered relevant to risk assessment, regulatory decision-making, and/or environmental management. They are intended to aggregate knowledge currently dispersed in journal articles, textbooks, reports, databases, and many other sources into a systematic and accessible format that facilitates use of that knowledge to support a broad diversity of applications and interests.
    </p>
    <h2>What is MIE?</h2>
    <p>
      Within the AOP framework, a molecular initiating event (MIE) is defined as the first point of chemical-biological interaction within an organism that starts the AOP.
    </p>

    <h2>What is KE?</h2>
    <p>
      The key event (KE), as defined by a set structured ontology terms consisting of a biological process, object, and action with each term originating from one of 14 biological ontologies. Biological process describes dynamics of the underlying biological system (e.g., receptor signaling).
    </p>
    <p>
      Biological process describes dynamics of the underlying biological system (e.g., receptor signaling).  The biological object is the subject of the perturbation (e.g., a specific biological receptor that is activated or inhibited). Action represents the direction of perturbation of this system (generally increased or decreased; e.g., ‘decreased’ in the case of a receptor that is inhibited to indicate a decrease in the signaling by that receptor).
    </p>
    <h2>AOPs are described according to five key principles.</h2>
    <ol>
      <li>
        AOPs are not stressor specific – They describe generalized motifs of biological response to a specific type of biological perturbation that apply without regard to which specific stressor initiated the response(s).
      </li>
      <li>
        AOPs are modular – AOPs are described using two basic units of information: (1) key events that describe measurable biological responses with some understood causal relationship to downstream biological change; and (2) key event relationships that lay out the evidence supporting a causal relationship between any pair of key events (one causing the other) and our understanding under which event A can cause event B to occur. Those units may be shared among multiple AOPs.
      </li>
      <li>
        AOPs are a pragmatic unit of development and review – An individual AOP defines a single sequence of biological events leading from a single initiating event to a single adverse outcome. It is recognized that biological response to stressors is not that simplistic. However, an AOP does not try to capture ALL possible outcomes or pathways to those outcomes, just a single pathway from initiation to outcome. This is considered a tractable unit for development and review.
      </li>
      <li>
        Modular AOPs can assemble into AOP networks – An AOP network is a group of two or more AOPs that share one or more key events. Assembly of individual AOPs into AOP networks provides the ability to capture the greater complexity of biological responses in an organized and systematic way.
      </li>
      <li>
        AOPs are living documents – Because AOP descriptions are a synthesis and integration of existing knowledge, they reflect the current state of science and knowledge and the AOP authors expertise and investment in organizing and communicating the current state. AOPs with knowledge gaps and uncertainties can still be useful, and even after publication, AOPs can continue to evolve as new data and knowledge are generated. Thus, AOPs should be evaluated based on the extent to which they capture current understanding and effectively communicate it to non-specialist audiences that need to use it, not whether they can address all questions concerning the impact of a given biological perturbation.
      </li>
    </ol>
  </div>

</template>

<style scoped>
.predict-text {
  /* 中间介绍字体的样式*/
  font-size: 18px;
  margin-top: 20px;
  width: 80%; /* 设置最大宽度，以避免文本过长 */
  text-align: justify; /* 文本两端对齐 */
  line-height: 200%; /* 设置行高 */
}
.markdown {
  font-family: Arial, sans-serif;
  line-height: 1.6;
  margin: 20px;
}

h1 {
  color: #333;
  font-size: 24px;
  margin-bottom: 10px;
}

p {
  color: #555;
  margin-bottom: 15px;
}

ol {
  margin-left: 20px; /* 添加左边距以缩进列表 */
}

li {
  color: #555;
  margin-bottom: 15px;
}
</style>
