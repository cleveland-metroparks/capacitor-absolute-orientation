export interface AbsoluteOrientationPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
