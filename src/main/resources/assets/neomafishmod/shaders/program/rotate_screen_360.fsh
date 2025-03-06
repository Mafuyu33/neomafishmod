#version 150

uniform sampler2D DiffuseSampler;
uniform float time;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    // 计算旋转角度（单位：弧度）
    float angle = time; // 这里 time 可以传入 Minecraft 游戏的 tick 时间，或者 sin(time) 来实现循环旋转

    // 计算旋转矩阵
    float cosA = cos(angle);
    float sinA = sin(angle);

    // 将 UV 坐标中心从 (0,0) 平移到 (0.5,0.5)，再应用旋转
    vec2 centeredUV = texCoord - vec2(0.5);
    vec2 rotatedUV = vec2(
    cosA * centeredUV.x - sinA * centeredUV.y,
    sinA * centeredUV.x + cosA * centeredUV.y
    ) + vec2(0.5); // 旋转后再移回中心

    // 采样纹理
    vec4 color = texture(DiffuseSampler, rotatedUV);
    fragColor = color;
}