#version 150

uniform sampler2D AllWhiteSampler;
// 纹理每个像素对应的偏移量：(1/textureWidth, 1/textureHeight)
uniform vec2 oneTexel;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    // 在 3×3 邻域内采样（取红色通道或你纹理的灰度值）
    float tl = texture(AllWhiteSampler, texCoord + oneTexel * vec2(-1, -1)).r;
    float  t = texture(AllWhiteSampler, texCoord + oneTexel * vec2(0, -1)).r;
    float tr = texture(AllWhiteSampler, texCoord + oneTexel * vec2(1, -1)).r;

    float  l = texture(AllWhiteSampler, texCoord + oneTexel * vec2(-1, 0)).r;
    float  c = texture(AllWhiteSampler, texCoord).r;
    float  r = texture(AllWhiteSampler, texCoord + oneTexel * vec2(1, 0)).r;

    float bl = texture(AllWhiteSampler, texCoord + oneTexel * vec2(-1, 1)).r;
    float  b = texture(AllWhiteSampler, texCoord + oneTexel * vec2(0, 1)).r;
    float br = texture(AllWhiteSampler, texCoord + oneTexel * vec2(1, 1)).r;

    // Sobel 核
    float gx = -tl - 2.0 * l - bl + tr + 2.0 * r + br;
    float gy = -tl - 2.0 * t - tr + bl + 2.0 * b + br;

    // 梯度幅值
    float edge = length(vec2(gx, gy));

    // 可选：增强对比度
    edge = clamp(edge * 0.5, 0.0, 1.0);
    if (edge < 0.1){
        discard;
    }

    fragColor = vec4(1, 1, 1, 1.0);
}