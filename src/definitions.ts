export interface SafeAreaPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
