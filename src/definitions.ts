export interface LionflenceImapPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
