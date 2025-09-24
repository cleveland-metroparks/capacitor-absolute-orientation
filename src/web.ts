import { WebPlugin } from '@capacitor/core';

import type { AbsoluteOrientationPlugin, AbsoluteOrientationReading, CallbackId } from './definitions';

export class AbsoluteOrientationWeb extends WebPlugin implements AbsoluteOrientationPlugin {
  start(): Promise<void> {
    throw new Error('Method not implemented.');
  }
  stop(): Promise<void> {
    throw new Error('Method not implemented.');
  }
  isActivated(): Promise<{ value: boolean }> {
    throw new Error('Method not implemented.');
  }
  addReadingListener(callback: (reading: AbsoluteOrientationReading) => void): Promise<CallbackId> {
    throw new Error('Method not implemented.');
  }
  removeReadingListener(id: CallbackId): Promise<void> {
    throw new Error('Method not implemented.');
  }
}
