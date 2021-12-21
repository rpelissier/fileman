const url = process.env.URL;

export async function loadDir(path: string): Promise<string> {

  const requestBody = {
    path: path,
  };

  return new Promise<string>(async function (resolve, reject) {
    const res = await fetch(`${url}/directory`, {
      method: "post",
      headers: {
        "Content-Type": "application/json",
        "x-access-token": "token-value",
      },
      body: JSON.stringify(requestBody),
    });

    if (!res.ok) {
      const message = `Fetch error: ${res.status} - ${res.statusText}`;
      reject(message);
      return;
    }

    const data = await res.json();

    resolve(data);
  });
}
