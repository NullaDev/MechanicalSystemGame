'use strict'

import * as gl from "./gl";
import * as Screen from "./Screen";

export var devicePixelRatio = 1;
export var last_update_rate = 1;
export var canvas_width: number;
export var canvas_height: number;

function setup_listener() {

}

export function setup_canvas() {
    setup_listener();
    var winw = window.innerWidth;
    var winh = window.innerHeight;
    var winr = Math.min(winw / Screen.SCR_HALF_WIN_WIDTH, winh / Screen.SCR_HALF_WIN_HEIGHT) * 0.95;
    var canvas = <HTMLCanvasElement>document.getElementById("glcanvas");
    canvas_width = winr * Screen.SCR_HALF_WIN_WIDTH;
    canvas_height = winr * Screen.SCR_HALF_WIN_HEIGHT;
    canvas.style.width = canvas_width + "px";
    canvas.style.height = canvas_height + "px";
    devicePixelRatio = window.devicePixelRatio || 1;
    canvas.width = canvas_width * devicePixelRatio;
    canvas.height = canvas_height * devicePixelRatio;
    gl.setup();
}

const container = <any>window;
export const debug_info: {
} = {
};
container.debug_info = debug_info;
