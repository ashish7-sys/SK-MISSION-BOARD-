document.addEventListener('DOMContentLoaded', function () {
  let canvas = document.getElementById('glow-sweep-canvas');
  if (!canvas) {
    canvas = document.createElement('canvas');
    canvas.id = 'glow-sweep-canvas';
  }
  
  // Ensure canvas is directly prepended as the first child of body
  if (document.body && document.body.firstChild !== canvas) {
    document.body.prepend(canvas);
  }

  console.log('[GlowSweep] Canvas element initialized and prepended to body:', canvas);

  const ctx = canvas.getContext('2d');
  if (!ctx) {
    console.error('[GlowSweep] 2D Context missing on canvas');
    return;
  }

  let waves = [];
  let colorIndex = 0;

  // 20 Bright Sequential Neon Colors
  const colors = [
    '#00f3ff', '#ffe600', '#00ff66', '#ff0055', '#ff00a0',
    '#9d00ff', '#ff6600', '#a6ff00', '#00a6ff', '#ffd700',
    '#e000ff', '#00ffd5', '#ff4365', '#00ff9f', '#ffaa00',
    '#3a00ff', '#ff003c', '#00e5ff', '#d4ff00', '#ff007f'
  ];

  function getWidth() {
    return Math.max(document.documentElement ? document.documentElement.clientWidth : 0, window.innerWidth || 0, 360);
  }

  function getHeight() {
    return Math.max(document.documentElement ? document.documentElement.clientHeight : 0, window.innerHeight || 0, 640);
  }

  function resize() {
    const dpr = window.devicePixelRatio || 1;
    const w = getWidth();
    const h = getHeight();
    canvas.width = w * dpr;
    canvas.height = h * dpr;
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
  }

  window.addEventListener('resize', resize);
  resize();

  function addWave(clientX, clientY) {
    const w = getWidth();
    const h = getHeight();
    const maxR = Math.hypot(w, h) * 1.2;
    waves.push({
      x: clientX,
      y: clientY,
      r: 5,
      maxR: maxR,
      color: colors[colorIndex],
      lineWidth: 2,
      speed: 8,
      alpha: 1.0
    });
    colorIndex = (colorIndex + 1) % colors.length;
  }

  // Pre-populate with initial test wave on load for visual verification
  addWave(getWidth() / 2, getHeight() / 2);

  // Pointerdown capture
  window.addEventListener('pointerdown', (e) => {
    addWave(e.clientX, e.clientY);
  }, { capture: true, passive: true });

  // Touchstart fallback for Android WebView
  window.addEventListener('touchstart', (e) => {
    if (e.touches && e.touches.length > 0) {
      for (let i = 0; i < e.changedTouches.length; i++) {
        const t = e.changedTouches[i];
        addWave(t.clientX, t.clientY);
      }
    }
  }, { capture: true, passive: true });

  function render() {
    try {
      const wWidth = getWidth();
      const wHeight = getHeight();
      ctx.clearRect(0, 0, wWidth, wHeight);

      for (let i = waves.length - 1; i >= 0; i--) {
        const w = waves[i];
        w.r += w.speed;
        w.alpha = Math.max(0, 1 - (w.r / w.maxR));

        if (w.alpha > 0.01) {
          ctx.save();
          ctx.globalAlpha = w.alpha;

          // Android WebView Safe Glow using Radial Gradient & Dual Arc Strokes
          const innerR = Math.max(0, w.r - w.lineWidth);
          const outerR = w.r + w.lineWidth;
          
          // Outer Soft Glow Pass using Gradient Arc
          try {
            const grad = ctx.createRadialGradient(w.x, w.y, innerR, w.x, w.y, outerR);
            grad.addColorStop(0, 'transparent');
            grad.addColorStop(0.5, w.color);
            grad.addColorStop(1, 'transparent');

            ctx.beginPath();
            ctx.arc(w.x, w.y, w.r, 0, Math.PI * 2);
            ctx.strokeStyle = grad;
            ctx.lineWidth = w.lineWidth * 1.8;
            ctx.stroke();
          } catch (e) {
            // Fallback stroke if radial gradient fails
            ctx.beginPath();
            ctx.arc(w.x, w.y, w.r, 0, Math.PI * 2);
            ctx.strokeStyle = w.color;
            ctx.lineWidth = w.lineWidth;
            ctx.stroke();
          }

          // Core Crisp Arc Pass
          ctx.beginPath();
          ctx.arc(w.x, w.y, w.r, 0, Math.PI * 2);
          ctx.strokeStyle = '#ffffff';
          ctx.lineWidth = w.lineWidth * 0.35;
          ctx.stroke();

          ctx.restore();
        } else {
          waves.splice(i, 1);
        }
      }
    } catch (err) {
      console.error('[GlowSweep] Render error:', err);
    }
    requestAnimationFrame(render);
  }

  render();
});
