//#version 150
//
//in vec4 Position;
//
//out vec2 texCoord;
//
//void main(){
//    // gl_Position 正常
//    gl_Position = vec4(Position.xy,0.0, 1.0);
//    // 把 [-1..1] 映射到 [0..1] => (x+1)/2
//    texCoord = (Position.xy * 0.5) + 0.5;
//}
//这个没用，vertex着色器用原版的blit了