export const enum Sprite_Mode { Overlay, AddBlend }
export const enum Category { Special, Small, Medium, Large }

export type SpriteSource = {
    path: string,
    w: number,
    h: number
}

export type Sprite = {
    sprite: SpriteSource,
    tx: number,
    ty: number,
    tw: number,
    th: number,
    mode: Sprite_Mode,
    category: Category,
    type: number,
    color: number,
    omega: number
};

export class SpriteRenderer {

    public readonly sprite: Sprite;
    private readonly sw: number;
    private readonly sh: number;
    public texs: { x: number, y: number }[];
    public rate: number;

    constructor(s: Sprite, texs: { x: number, y: number }[], rate: number) {
        this.sprite = s;
        this.sw = s.sprite.w;
        this.sh = s.sprite.h;
        this.texs = texs;
        this.rate = rate;
    }

    public setAlternation(texs: { x: number, y: number }[], rate: number) {
        this.texs = texs;
        this.rate = rate;
    }

    public injectXYWH(arr: Float32Array, index: number, time: number): void {
        const tex = this.texs[Math.floor(time / this.rate) % this.texs.length];
        const tx = this.sprite.tx + tex.x;
        const ty = this.sprite.ty + tex.y;
        arr[index + 0] = tx / this.sw;
        arr[index + 1] = ty / this.sh;
        arr[index + 2] = this.sprite.tw / this.sw;
        arr[index + 3] = this.sprite.th / this.sh;
    }

    public pushXYWH(arr: number[], time: number): void {
        const tex = this.texs[Math.floor(time / this.rate) % this.texs.length];
        const tx = this.sprite.tx + tex.x;
        const ty = this.sprite.ty + tex.y;
        arr.push(tx / this.sw);
        arr.push(ty / this.sh);
        arr.push(this.sprite.tw / this.sw);
        arr.push(this.sprite.th / this.sh);
    }

    public setXYWH(obj: any, time: number): any {
        const tex = this.texs[Math.floor(time / this.rate) % this.texs.length];
        const tx = this.sprite.tx + tex.x;
        const ty = this.sprite.ty + tex.y;
        obj.tx = tx / this.sw;
        obj.ty = ty / this.sh;
        obj.tw = this.sprite.tw / this.sw;
        obj.th = this.sprite.th / this.sh;
        return obj;
    }

}
