import * as gl from "./gl";
import { Sprite, Sprite_Mode } from "./Sprite";

export interface RECT {
    rectCount(): number,
    sprite(ind: number): Sprite,
    render(arr: Float32Array, ind: number, offset: number): void
}

export class SpriteManager {

    private static INS: { [key: string]: SpriteManager } = {};
    private path: string;

    public img: any = null;

    constructor(url: string) {
        this.path = url;
    }

    public loaded(): boolean {
        return this.img;
    }

    public async load(): Promise<void> {
        const img = await gl.loadImage(this.path);
        this.img = gl.loadTexture(img);
    }

    public static get(url: string): SpriteManager {
        if (SpriteManager.INS[url])
            return SpriteManager.INS[url];
        return (SpriteManager.INS[url] = new SpriteManager(url));
    }

    public draw(list: RECT[]) {
        this.drawRect(list, Sprite_Mode.Overlay);
        this.drawRect(list, Sprite_Mode.AddBlend);

    }

    private drawRect(list: RECT[], mode: Sprite_Mode) {
        var rectn = 0;
        for (var e of list) {
            const n = e.rectCount();
            for (var i = 0; i < n; i++) {
                if (e.sprite(i).mode == mode)
                    rectn++;
            }
        }
        if (rectn)
            gl.setMode(mode);
        else return;
        var xyrwh = new Float32Array(rectn * 10);
        var count = 0;
        for (var e of list) {
            const n = e.rectCount();
            for (var i = 0; i < n; i++) {
                if (e.sprite(i).mode == mode) {
                    e.render(xyrwh, i, count * 10);
                    count += 10;
                }
            }
        }
        gl.drawRects(xyrwh, rectn, this.img);
    }

}