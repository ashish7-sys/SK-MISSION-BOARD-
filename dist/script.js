(function () {
  const canvas = document.getElementById('glow-sweep-canvas');
  if (!canvas) return;

  const ctx = canvas.getContext('2d');
  let waves = [];
  let colorIndex = 0;

  // 20 Bright Sequential Neon Colors (Same as reference video)
  const colors = [
    '#00f3ff', '#ffe600', '#00ff66', '#ff0055', '#ff00a0',
    '#9d00ff', '#ff6600', '#a6ff00', '#00a6ff', '#ffd700',
    '#e000ff', '#00ffd5', '#ff4365', '#00ff9f', '#ffaa00',
    '#3a00ff', '#ff003c', '#00e5ff', '#d4ff00', '#ff007f'
  ];

  function resize() {
    canvas.width = window.innerWidth * window.devicePixelRatio;
    canvas.height = window.innerHeight * window.devicePixelRatio;
    ctx.scale(window.devicePixelRatio, window.devicePixelRatio);
  }

  window.addEventListener('resize', resize);
  resize();

  // Multi-tap touch listener
  window.addEventListener('pointerdown', (e) => {
    const maxR = Math.hypot(window.innerWidth, window.innerHeight) * 1.2;
    waves.push({
      x: e.clientX,
      y: e.clientY,
      r: 10,
      maxR: maxR,
      color: colors[colorIndex],
      lineWidth: 24,
      speed: 18,
      alpha: 1.0
    });
    colorIndex = (colorIndex + 1) % colors.length; // Loop 20 colors
  }, { passive: true });

  function render() {
    ctx.clearRect(0, 0, window.innerWidth, window.innerHeight);

    for (let i = waves.length - 1; i >= 0; i--) {
      const w = waves[i];
      w.r += w.speed;
      w.alpha = Math.max(0, 1 - (w.r / w.maxR));

      if (w.alpha > 0) {
        ctx.save();
        ctx.globalAlpha = w.alpha;
        
        // Expanding Radial Glow Wave
        ctx.beginPath();
        ctx.arc(w.x, w.y, w.r, 0, Math.PI * 2);
        
        ctx.strokeStyle = w.color;
        ctx.lineWidth = w.lineWidth;
        ctx.shadowColor = w.color;
        ctx.shadowBlur = 30; // Bright glow outline
        ctx.stroke();
        
        ctx.restore();
      } else {
        waves.splice(i, 1); // Auto cleanup memory
      }
    }
    requestAnimationFrame(render);
  }

  render();
})();
