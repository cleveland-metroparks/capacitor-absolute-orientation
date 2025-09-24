export type CallbackId = string;

export interface AbsoluteOrientationPlugin {
  start(): Promise<void>;
  stop(): Promise<void>;
  isActivated(): Promise<{ value: boolean }>;
  addReadingListener(callback: (reading: AbsoluteOrientationReading) => void): Promise<CallbackId>;
  removeReadingListener(id: CallbackId): Promise<void>;
}

export interface AbsoluteOrientationReading {
  timestamp: number;
  quaternion: [number, number, number, number];
  alpha: number; // Z-Axis, [0,360)
  beta: number; // X-Axis, [-180,180)
  gamma: number; // Y-Axis, [90,90)
}
