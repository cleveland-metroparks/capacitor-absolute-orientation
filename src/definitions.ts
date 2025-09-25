export type CallbackId = string;

export interface AbsoluteOrientationPlugin {
	start(): Promise<void>;
	stop(): Promise<void>;
	isActivated(): Promise<{ value: boolean }>;
	addReadingListener(callback: (reading: AbsoluteOrientationReading) => void): Promise<CallbackId>;
	removeReadingListener(id: CallbackId): Promise<void>;
}

export interface Quaternion {
	x: number;
	y: number;
	z: number;
	w: number;
}

export interface AbsoluteOrientationReading {
	timestamp: number;
	quaternion?: Quaternion;
	compassHeading: number;
}
