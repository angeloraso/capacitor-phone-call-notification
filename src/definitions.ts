export interface PhoneCallNotificationPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
