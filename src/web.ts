import { WebPlugin } from '@capacitor/core';

import type {
	AbsoluteOrientationPlugin,
	AbsoluteOrientationReading,
	CallbackId,
	Quaternion
} from './definitions';

interface DeviceOrientationEventIos extends DeviceOrientationEvent {
	requestPermission?: () => Promise<'granted' | 'denied'>;
	readonly webkitCompassHeading: number | null | undefined;
}

type WindowListener = {
	event: 'deviceorientation' | 'deviceorientationabsolute';
	handler: (event: DeviceOrientationEvent) => void;
};

type ReadingListener = (reading: AbsoluteOrientationReading) => void;

export class AbsoluteOrientationWeb extends WebPlugin implements AbsoluteOrientationPlugin {
	private _windowListeners = new Array<WindowListener>();
	private _active = false;
	private _callbackIdCount = 0;
	private _readingListeners = new Map<string, ReadingListener>();

	public async start(): Promise<void> {
		if (!window.isSecureContext) {
			// TODO: Use unified errors across platforms
			throw new Error('Not supported in insecure context');
		}
		const requestPermission = (DeviceOrientationEvent as unknown as DeviceOrientationEventIos)
			.requestPermission;
		const isIos = typeof requestPermission === 'function';
		if (isIos) {
			requestPermission().then((res) => {
				if (res === 'granted') {
					const handler = (event: DeviceOrientationEvent) => {
						this._deviceOrientationHandlerIos(event as unknown as DeviceOrientationEventIos);
					};
					window.addEventListener('deviceorientation', handler);
					this._windowListeners.push({ event: 'deviceorientation', handler });
					this._active = true;
				} else {
					// TODO: Use unified errors
					throw new Error('Permission denied');
				}
			});
		} else {
			if (window.ondeviceorientationabsolute !== undefined) {
				const handler = (event: DeviceOrientationEvent) => {
					this._deviceOrientationAbsoluteHandler(event);
				};
				window.addEventListener('deviceorientationabsolute', handler);
				this._windowListeners.push({ event: 'deviceorientationabsolute', handler });
			} else {
				const handler = (event: DeviceOrientationEvent) => {
					this._deviceOrientationHandler(event);
				};
				window.addEventListener('deviceorientation', handler);
				this._windowListeners.push({ event: 'deviceorientation', handler });
			}
			this._active = true;
		}
	}

	public async stop(): Promise<void> {
		if (!this._active) {
			return;
		}
		for (const listener of this._windowListeners) {
			window.removeEventListener(listener.event, listener.handler);
		}
		this._active = false;
	}

	public async isActivated(): Promise<{ value: boolean }> {
		return { value: this._active };
	}

	public async addReadingListener(
		callback: (reading: AbsoluteOrientationReading) => void
	): Promise<CallbackId> {
		const id = (this._callbackIdCount++).toString();
		this._readingListeners.set(id, callback);
		return id;
	}

	public async removeReadingListener(id: CallbackId): Promise<void> {
		this._readingListeners.delete(id);
	}

	private _deviceOrientationHandlerIos(event: DeviceOrientationEventIos) {
		if (event.webkitCompassHeading === null || event.webkitCompassHeading === undefined) {
			return;
		}
		const reading: AbsoluteOrientationReading = {
			timestamp: new Date().valueOf(),
			compassHeading: 360 - event.webkitCompassHeading
		};
		this._sendReading(reading);
	}

	private _deviceOrientationAbsoluteHandler(event: DeviceOrientationEvent) {
		if (event.alpha === null || event.beta === null || event.gamma === null) {
			return;
		}
		const x = this._degToRad(event.beta);
		const y = this._degToRad(event.alpha);
		const z = -this._degToRad(event.gamma);

		const cX = Math.cos(x / 2.0);
		const cY = Math.cos(y / 2.0);
		const cZ = Math.cos(z / 2.0);

		const sX = Math.sin(x / 2.0);
		const sY = Math.sin(y / 2.0);
		const sZ = Math.sin(z / 2.0);

		const quaternion: Quaternion = {
			x: sX * cY * cZ + cX * sY * sZ,
			y: cX * sY * cZ - sX * cY * sZ,
			z: cX * cY * sZ - sX * sY * cZ,
			w: cX * cY * cZ + sX * sY * sZ
		};
		const magnitude = Math.sqrt(
			quaternion.w * quaternion.w +
				quaternion.x * quaternion.x +
				quaternion.y * quaternion.y +
				quaternion.z * quaternion.z
		);
		quaternion.w /= magnitude;
		quaternion.x /= magnitude;
		quaternion.y /= magnitude;
		quaternion.z /= magnitude;
		const reading: AbsoluteOrientationReading = {
			timestamp: new Date().valueOf(),
			quaternion,
			compassHeading: this._quaternionToCompassHeading(quaternion)
		};
		this._sendReading(reading);
	}

	private _deviceOrientationHandler(event: DeviceOrientationEvent) {
		if (event.absolute === true) {
			this._deviceOrientationAbsoluteHandler(event);
		}
	}

	private _sendReading(reading: AbsoluteOrientationReading) {
		for (const [, callback] of this._readingListeners) {
			callback(reading);
		}
	}

	private _degToRad(degrees: number): number {
		return degrees * (Math.PI / 180.0);
	}

	private _radToDeg(radians: number): number {
		return radians * (180.0 / Math.PI);
	}

	private _quaternionToCompassHeading(quaternion: Quaternion): number {
		const x = quaternion.x;
		const y = quaternion.y;
		const z = quaternion.z;
		const w = quaternion.w;
		const yaw = Math.atan2(2.0 * (x * z - w * y), 1.0 - 2.0 * (y * y + z * z));
		let heading = this._radToDeg(-yaw);
		if (heading < 0) {
			heading += 360;
		}
		return heading;
	}
}
